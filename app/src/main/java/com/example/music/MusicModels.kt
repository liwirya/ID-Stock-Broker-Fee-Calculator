package com.example.music

import java.util.Locale

enum class RepeatMode {
    OFF,
    REPEAT_ALL,
    REPEAT_ONE;

    fun next(): RepeatMode = when (this) {
        OFF -> REPEAT_ALL
        REPEAT_ALL -> REPEAT_ONE
        REPEAT_ONE -> OFF
    }
}

data class SavedTrack(
    val id: String,
    val title: String,
    val fileName: String
)

data class MusicPlayerState(
    val tracks: List<SavedTrack> = emptyList(),
    val currentTrackIndex: Int = -1,
    val isPlaying: Boolean = false,
    val positionMs: Long = 0L,
    val durationMs: Long = 0L,
    val repeatMode: RepeatMode = RepeatMode.REPEAT_ALL
) {
    val currentTrack: SavedTrack?
        get() = tracks.getOrNull(currentTrackIndex)
}

fun formatMusicDuration(ms: Long): String {
    val totalSeconds = (ms.coerceAtLeast(0L) / 1000L)
    val minutes = totalSeconds / 60L
    val seconds = totalSeconds % 60L
    return String.format(Locale.US, "%d:%02d", minutes, seconds)
}
