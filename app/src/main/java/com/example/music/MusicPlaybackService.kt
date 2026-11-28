package com.example.music

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import androidx.core.content.ContextCompat
import com.example.MainActivity
import com.example.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

object MusicPlayerController {
    private val _state = MutableStateFlow(MusicPlayerState())
    val state: StateFlow<MusicPlayerState> = _state.asStateFlow()

    internal fun updateState(transform: (MusicPlayerState) -> MusicPlayerState) {
        _state.update(transform)
    }

    suspend fun syncSavedTracks(context: Context) {
        val repo = MusicPlaylistRepository(context)
        val snapshot = repo.loadSnapshot()
        _state.update { current ->
            val tracks = snapshot.tracks
            val activeIndex = when {
                tracks.isEmpty() -> -1
                current.currentTrackIndex in tracks.indices -> current.currentTrackIndex
                snapshot.lastTrackIndex in tracks.indices -> snapshot.lastTrackIndex
                else -> 0
            }
            current.copy(
                tracks = tracks,
                currentTrackIndex = activeIndex,
                repeatMode = if (current.isPlaying) current.repeatMode else snapshot.repeatMode
            )
        }
    }

    suspend fun importTracks(context: Context, uris: List<Uri>): Int {
        val repo = MusicPlaylistRepository(context)
        val (updatedTracks, addedCount) = repo.importAudioUris(uris)
        if (addedCount > 0) {
            val wasEmpty = _state.value.tracks.isEmpty()
            val firstAddedIndex = (updatedTracks.size - addedCount).coerceAtLeast(0)
            _state.update { current ->
                current.copy(
                    tracks = updatedTracks,
                    currentTrackIndex = if (current.currentTrackIndex !in updatedTracks.indices) {
                        firstAddedIndex
                    } else {
                        current.currentTrackIndex
                    }
                )
            }
            if (wasEmpty) {
                playTrackAt(context, firstAddedIndex)
            }
        }
        return addedCount
    }

    fun playTrackAt(context: Context, index: Int) {
        sendCommand(context, MusicPlaybackService.ACTION_PLAY_INDEX) {
            putExtra(MusicPlaybackService.EXTRA_INDEX, index)
        }
    }

    fun togglePlayPause(context: Context) {
        sendCommand(context, MusicPlaybackService.ACTION_TOGGLE_PLAY_PAUSE)
    }

    fun playNext(context: Context) {
        sendCommand(context, MusicPlaybackService.ACTION_NEXT)
    }

    fun playPrevious(context: Context) {
        sendCommand(context, MusicPlaybackService.ACTION_PREVIOUS)
    }

    fun cycleRepeatMode(context: Context) {
        val nextMode = _state.value.repeatMode.next()
        _state.update { it.copy(repeatMode = nextMode) }
        sendCommand(context, MusicPlaybackService.ACTION_SET_REPEAT) {
            putExtra(MusicPlaybackService.EXTRA_REPEAT_MODE, nextMode.name)
        }
    }

    fun seekTo(context: Context, positionMs: Long) {
        _state.update { it.copy(positionMs = positionMs.coerceIn(0L, it.durationMs.coerceAtLeast(0L))) }
        sendCommand(context, MusicPlaybackService.ACTION_SEEK) {
            putExtra(MusicPlaybackService.EXTRA_POSITION_MS, positionMs)
        }
    }

    suspend fun deleteTrack(context: Context, trackId: String) {
        val currentState = _state.value
        val deletedIdx = currentState.tracks.indexOfFirst { it.id == trackId }
        val repo = MusicPlaylistRepository(context)
        val updatedTracks = repo.deleteTrack(trackId)

        if (deletedIdx < 0) return

        val wasCurrent = deletedIdx == currentState.currentTrackIndex
        val newIndex = when {
            updatedTracks.isEmpty() -> -1
            deletedIdx < currentState.currentTrackIndex -> currentState.currentTrackIndex - 1
            wasCurrent -> deletedIdx.coerceAtMost(updatedTracks.lastIndex)
            else -> currentState.currentTrackIndex.coerceIn(updatedTracks.indices)
        }

        _state.update { state ->
            state.copy(
                tracks = updatedTracks,
                currentTrackIndex = newIndex,
                isPlaying = if (updatedTracks.isEmpty() || wasCurrent) false else state.isPlaying,
                positionMs = if (updatedTracks.isEmpty() || wasCurrent) 0L else state.positionMs,
                durationMs = if (updatedTracks.isEmpty() || wasCurrent) 0L else state.durationMs
            )
        }

        if (updatedTracks.isEmpty() || wasCurrent) {
            sendCommand(context, MusicPlaybackService.ACTION_STOP)
        }
    }

    private fun sendCommand(
        context: Context,
        action: String,
        configure: Intent.() -> Unit = {}
    ) {
        val intent = Intent(context.applicationContext, MusicPlaybackService::class.java).apply {
            this.action = action
            configure()
        }
        val shouldStartForeground = action in setOf(
            MusicPlaybackService.ACTION_PLAY_INDEX,
            MusicPlaybackService.ACTION_TOGGLE_PLAY_PAUSE,
            MusicPlaybackService.ACTION_NEXT,
            MusicPlaybackService.ACTION_PREVIOUS
        )
        try {
            if (shouldStartForeground && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                ContextCompat.startForegroundService(context.applicationContext, intent)
            } else {
                context.applicationContext.startService(intent)
            }
        } catch (_: Exception) {
            try {
                context.applicationContext.startService(intent)
            } catch (_: Exception) {
            }
        }
    }
}

class MusicPlaybackService : Service() {

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
    private lateinit var repository: MusicPlaylistRepository

    private var mediaPlayer: MediaPlayer? = null
    private var preparedTrackId: String? = null
    private var progressJob: Job? = null

    override fun onCreate() {
        super.onCreate()
        repository = MusicPlaylistRepository(applicationContext)
        createNotificationChannel()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val action = intent?.action
        when (action) {
            ACTION_PLAY_INDEX -> {
                val index = intent.getIntExtra(EXTRA_INDEX, 0)
                serviceScope.launch {
                    ensureTracksLoaded()
                    playTrackByIndex(index)
                }
            }
            ACTION_TOGGLE_PLAY_PAUSE -> {
                serviceScope.launch {
                    ensureTracksLoaded()
                    togglePlayPauseInternal()
                }
            }
            ACTION_NEXT -> {
                serviceScope.launch {
                    ensureTracksLoaded()
                    skipRelative(delta = 1, userInitiated = true)
                }
            }
            ACTION_PREVIOUS -> {
                serviceScope.launch {
                    ensureTracksLoaded()
                    skipRelative(delta = -1, userInitiated = true)
                }
            }
            ACTION_SET_REPEAT -> {
                val modeName = intent.getStringExtra(EXTRA_REPEAT_MODE)
                val mode = modeName?.let { runCatching { RepeatMode.valueOf(it) }.getOrNull() }
                if (mode != null) {
                    MusicPlayerController.updateState { it.copy(repeatMode = mode) }
                    serviceScope.launch {
                        repository.savePlaybackPreferences(
                            lastIndex = MusicPlayerController.state.value.currentTrackIndex,
                            repeatMode = mode
                        )
                    }
                }
            }
            ACTION_SEEK -> {
                val pos = intent.getLongExtra(EXTRA_POSITION_MS, 0L)
                seekInternal(pos)
            }
            ACTION_STOP -> {
                stopPlaybackAndSelf()
            }
        }
        return START_NOT_STICKY
    }

    /**
     * Triggered when the user swipes the app away from Recents ("Close All").
     * Stops music immediately while still allowing uninterrupted playback when
     * the app is simply minimized (Home button / temporary close).
     */
    override fun onTaskRemoved(rootIntent: Intent?) {
        stopPlaybackAndSelf()
        super.onTaskRemoved(rootIntent)
    }

    override fun onDestroy() {
        releasePlayer()
        serviceScope.cancel()
        super.onDestroy()
    }

    private suspend fun ensureTracksLoaded() {
        if (MusicPlayerController.state.value.tracks.isEmpty()) {
            MusicPlayerController.syncSavedTracks(applicationContext)
        }
    }

    private fun togglePlayPauseInternal() {
        val state = MusicPlayerController.state.value
        val tracks = state.tracks
        if (tracks.isEmpty()) {
            stopPlaybackAndSelf()
            return
        }

        val mp = mediaPlayer
        val activeTrack = state.currentTrack
        if (mp != null && activeTrack != null && preparedTrackId == activeTrack.id) {
            if (mp.isPlaying) {
                mp.pause()
                stopProgressPolling()
                MusicPlayerController.updateState {
                    it.copy(
                        isPlaying = false,
                        positionMs = safeCurrentPosition(mp)
                    )
                }
                promoteToForeground(isPlaying = false, title = activeTrack.title)
            } else {
                mp.start()
                startProgressPolling()
                MusicPlayerController.updateState { it.copy(isPlaying = true) }
                promoteToForeground(isPlaying = true, title = activeTrack.title)
            }
        } else {
            val targetIndex = state.currentTrackIndex.takeIf { it in tracks.indices } ?: 0
            playTrackByIndex(targetIndex)
        }
    }

    private fun playTrackByIndex(requestedIndex: Int) {
        val tracks = MusicPlayerController.state.value.tracks
        if (tracks.isEmpty()) {
            stopPlaybackAndSelf()
            return
        }
        val index = requestedIndex.coerceIn(tracks.indices)
        val track = tracks[index]
        val file = repository.resolveTrackFile(track)
        if (!file.exists()) {
            stopPlaybackAndSelf()
            return
        }

        releasePlayer()
        try {
            val player = MediaPlayer().apply {
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
                )
                setDataSource(file.absolutePath)
                setOnPreparedListener { mp ->
                    preparedTrackId = track.id
                    mp.start()
                    val dur = mp.duration.toLong().coerceAtLeast(0L)
                    MusicPlayerController.updateState {
                        it.copy(
                            currentTrackIndex = index,
                            isPlaying = true,
                            positionMs = 0L,
                            durationMs = dur
                        )
                    }
                    startProgressPolling()
                    promoteToForeground(isPlaying = true, title = track.title)
                    serviceScope.launch {
                        repository.savePlaybackPreferences(index, MusicPlayerController.state.value.repeatMode)
                    }
                }
                setOnCompletionListener {
                    handleTrackCompleted()
                }
                setOnErrorListener { _, _, _ ->
                    MusicPlayerController.updateState { it.copy(isPlaying = false) }
                    true
                }
                prepareAsync()
            }
            mediaPlayer = player
            promoteToForeground(isPlaying = true, title = track.title)
        } catch (_: Exception) {
            stopPlaybackAndSelf()
        }
    }

    private fun handleTrackCompleted() {
        val state = MusicPlayerController.state.value
        val tracks = state.tracks
        if (tracks.isEmpty()) {
            stopPlaybackAndSelf()
            return
        }
        when (state.repeatMode) {
            RepeatMode.REPEAT_ONE -> {
                playTrackByIndex(state.currentTrackIndex.coerceIn(tracks.indices))
            }
            RepeatMode.REPEAT_ALL -> {
                val nextIndex = (state.currentTrackIndex + 1) % tracks.size
                playTrackByIndex(nextIndex)
            }
            RepeatMode.OFF -> {
                val nextIndex = state.currentTrackIndex + 1
                if (nextIndex in tracks.indices) {
                    playTrackByIndex(nextIndex)
                } else {
                    stopProgressPolling()
                    MusicPlayerController.updateState {
                        it.copy(isPlaying = false, positionMs = it.durationMs)
                    }
                    state.currentTrack?.let { promoteToForeground(isPlaying = false, title = it.title) }
                }
            }
        }
    }

    private fun skipRelative(delta: Int, userInitiated: Boolean) {
        val state = MusicPlayerController.state.value
        val tracks = state.tracks
        if (tracks.isEmpty()) {
            stopPlaybackAndSelf()
            return
        }
        val current = state.currentTrackIndex.takeIf { it in tracks.indices } ?: 0
        if (delta < 0 && safeCurrentPosition(mediaPlayer) > 3500L && userInitiated) {
            seekInternal(0L)
            return
        }
        val nextIndex = ((current + delta) % tracks.size + tracks.size) % tracks.size
        playTrackByIndex(nextIndex)
    }

    private fun seekInternal(positionMs: Long) {
        val mp = mediaPlayer ?: return
        try {
            val safe = positionMs.coerceIn(0L, mp.duration.toLong().coerceAtLeast(0L)).toInt()
            mp.seekTo(safe)
            MusicPlayerController.updateState { it.copy(positionMs = safe.toLong()) }
        } catch (_: Exception) {
        }
    }

    private fun startProgressPolling() {
        progressJob?.cancel()
        progressJob = serviceScope.launch {
            while (isActive) {
                val mp = mediaPlayer
                if (mp != null) {
                    try {
                        if (mp.isPlaying) {
                            MusicPlayerController.updateState {
                                it.copy(
                                    positionMs = mp.currentPosition.toLong().coerceAtLeast(0L),
                                    durationMs = mp.duration.toLong().coerceAtLeast(0L)
                                )
                            }
                        }
                    } catch (_: Exception) {
                    }
                }
                delay(400)
            }
        }
    }

    private fun stopProgressPolling() {
        progressJob?.cancel()
        progressJob = null
    }

    private fun safeCurrentPosition(mp: MediaPlayer?): Long {
        if (mp == null) return 0L
        return try {
            mp.currentPosition.toLong().coerceAtLeast(0L)
        } catch (_: Exception) {
            0L
        }
    }

    private fun releasePlayer() {
        stopProgressPolling()
        preparedTrackId = null
        try {
            mediaPlayer?.stop()
        } catch (_: Exception) {
        }
        try {
            mediaPlayer?.release()
        } catch (_: Exception) {
        }
        mediaPlayer = null
    }

    private fun stopPlaybackAndSelf() {
        releasePlayer()
        MusicPlayerController.updateState {
            it.copy(isPlaying = false, positionMs = 0L)
        }
        try {
            ServiceCompat.stopForeground(this, ServiceCompat.STOP_FOREGROUND_REMOVE)
        } catch (_: Exception) {
        }
        stopSelf()
    }

    private fun promoteToForeground(isPlaying: Boolean, title: String) {
        val notification = buildNotification(isPlaying = isPlaying, title = title)
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ServiceCompat.startForeground(
                    this,
                    NOTIFICATION_ID,
                    notification,
                    ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK
                )
            } else {
                startForeground(NOTIFICATION_ID, notification)
            }
        } catch (_: Exception) {
        }
    }

    private fun buildNotification(isPlaying: Boolean, title: String): Notification {
        val openAppIntent = PendingIntent.getActivity(
            this,
            0,
            Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            },
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val prevIntent = servicePendingIntent(ACTION_PREVIOUS, 101)
        val playPauseIntent = servicePendingIntent(ACTION_TOGGLE_PLAY_PAUSE, 102)
        val nextIntent = servicePendingIntent(ACTION_NEXT, 103)
        val stopIntent = servicePendingIntent(ACTION_STOP, 104)

        val playPauseIcon = if (isPlaying) {
            android.R.drawable.ic_media_pause
        } else {
            android.R.drawable.ic_media_play
        }
        val playPauseLabel = if (isPlaying) "Jeda" else "Putar"

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(getString(R.string.app_name))
            .setContentIntent(openAppIntent)
            .setOngoing(isPlaying)
            .setSilent(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .addAction(android.R.drawable.ic_media_previous, "Prev", prevIntent)
            .addAction(playPauseIcon, playPauseLabel, playPauseIntent)
            .addAction(android.R.drawable.ic_media_next, "Next", nextIntent)
            .addAction(android.R.drawable.ic_menu_close_clear_cancel, "Stop", stopIntent)
            .build()
    }

    private fun servicePendingIntent(action: String, requestCode: Int): PendingIntent {
        val intent = Intent(this, MusicPlaybackService::class.java).apply {
            this.action = action
        }
        return PendingIntent.getService(
            this,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                getString(R.string.music_notif_channel_name),
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = getString(R.string.music_notif_channel_desc)
                setShowBadge(false)
            }
            val manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(channel)
        }
    }

    companion object {
        private const val CHANNEL_ID = "fee_saham_music_playback"
        private const val NOTIFICATION_ID = 2101

        const val ACTION_PLAY_INDEX = "com.example.music.ACTION_PLAY_INDEX"
        const val ACTION_TOGGLE_PLAY_PAUSE = "com.example.music.ACTION_TOGGLE_PLAY_PAUSE"
        const val ACTION_NEXT = "com.example.music.ACTION_NEXT"
        const val ACTION_PREVIOUS = "com.example.music.ACTION_PREVIOUS"
        const val ACTION_SET_REPEAT = "com.example.music.ACTION_SET_REPEAT"
        const val ACTION_SEEK = "com.example.music.ACTION_SEEK"
        const val ACTION_STOP = "com.example.music.ACTION_STOP"

        const val EXTRA_INDEX = "extra_index"
        const val EXTRA_REPEAT_MODE = "extra_repeat_mode"
        const val EXTRA_POSITION_MS = "extra_position_ms"
    }
}
