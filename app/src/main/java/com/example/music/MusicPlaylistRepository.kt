package com.example.music

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

private val Context.musicPlaylistDataStore by preferencesDataStore(
    name = "fee_saham_music_prefs",
    corruptionHandler = ReplaceFileCorruptionHandler { emptyPreferences() }
)

data class PersistedMusicSnapshot(
    val tracks: List<SavedTrack>,
    val lastTrackIndex: Int,
    val repeatMode: RepeatMode
)

class MusicPlaylistRepository(private val context: Context) {

    private val appContext = context.applicationContext

    private object Keys {
        val TRACKS_JSON = stringPreferencesKey("saved_tracks_json")
        val LAST_INDEX = intPreferencesKey("last_track_index")
        val REPEAT_MODE = stringPreferencesKey("repeat_mode")
    }

    private fun musicDirectory(): File =
        File(appContext.filesDir, "saved_music").apply { mkdirs() }

    fun resolveTrackFile(track: SavedTrack): File =
        File(musicDirectory(), track.fileName)

    suspend fun loadSnapshot(): PersistedMusicSnapshot = withContext(Dispatchers.IO) {
        val prefs = try {
            appContext.musicPlaylistDataStore.data.first()
        } catch (_: Exception) {
            emptyPreferences()
        }
        val dir = musicDirectory()
        val rawJson = prefs[Keys.TRACKS_JSON]
        val parsedTracks = parseTracksJson(rawJson).filter { track ->
            File(dir, track.fileName).exists()
        }
        val savedIndex = prefs[Keys.LAST_INDEX] ?: if (parsedTracks.isNotEmpty()) 0 else -1
        val safeIndex = when {
            parsedTracks.isEmpty() -> -1
            savedIndex in parsedTracks.indices -> savedIndex
            else -> 0
        }
        val repeatMode = prefs[Keys.REPEAT_MODE]?.let { name ->
            runCatching { RepeatMode.valueOf(name) }.getOrNull()
        } ?: RepeatMode.REPEAT_ALL

        PersistedMusicSnapshot(
            tracks = parsedTracks,
            lastTrackIndex = safeIndex,
            repeatMode = repeatMode
        )
    }

    suspend fun importAudioUris(uris: List<Uri>): Pair<List<SavedTrack>, Int> = withContext(Dispatchers.IO) {
        val current = loadSnapshot().tracks.toMutableList()
        val dir = musicDirectory()
        var addedCount = 0

        for (uri in uris) {
            val displayName = resolveDisplayName(uri)
            val cleanTitle = displayName
                .substringBeforeLast('.')
                .ifBlank { "Lagu MP3 ${current.size + 1}" }
            val ext = displayName
                .substringAfterLast('.', missingDelimiterValue = "mp3")
                .lowercase()
                .filter { it.isLetterOrDigit() }
                .take(5)
                .ifBlank { "mp3" }
            val id = UUID.randomUUID().toString()
            val fileName = "track_${id}.$ext"
            val destFile = File(dir, fileName)

            val copied = try {
                appContext.contentResolver.openInputStream(uri)?.use { input ->
                    FileOutputStream(destFile).use { output ->
                        input.copyTo(output)
                    }
                }
                destFile.exists() && destFile.length() > 0L
            } catch (_: Exception) {
                destFile.delete()
                false
            }

            if (copied) {
                current.add(SavedTrack(id = id, title = cleanTitle, fileName = fileName))
                addedCount++
            }
        }

        if (addedCount > 0) {
            persistTracks(current)
        }
        current.toList() to addedCount
    }

    suspend fun deleteTrack(trackId: String): List<SavedTrack> = withContext(Dispatchers.IO) {
        val current = loadSnapshot().tracks.toMutableList()
        val target = current.firstOrNull { it.id == trackId }
        if (target != null) {
            current.removeAll { it.id == trackId }
            runCatching { resolveTrackFile(target).delete() }
            persistTracks(current)
        }
        current.toList()
    }

    suspend fun savePlaybackPreferences(lastIndex: Int, repeatMode: RepeatMode) {
        try {
            appContext.musicPlaylistDataStore.edit { prefs ->
                prefs[Keys.LAST_INDEX] = lastIndex
                prefs[Keys.REPEAT_MODE] = repeatMode.name
            }
        } catch (_: Exception) {
        }
    }

    private suspend fun persistTracks(tracks: List<SavedTrack>) {
        val array = JSONArray()
        tracks.forEach { track ->
            array.put(
                JSONObject().apply {
                    put("id", track.id)
                    put("title", track.title)
                    put("fileName", track.fileName)
                }
            )
        }
        try {
            appContext.musicPlaylistDataStore.edit { prefs ->
                prefs[Keys.TRACKS_JSON] = array.toString()
            }
        } catch (_: Exception) {
        }
    }

    private fun parseTracksJson(raw: String?): List<SavedTrack> {
        if (raw.isNullOrBlank()) return emptyList()
        return try {
            val array = JSONArray(raw)
            buildList {
                for (i in 0 until array.length()) {
                    val obj = array.optJSONObject(i) ?: continue
                    val id = obj.optString("id")
                    val title = obj.optString("title")
                    val fileName = obj.optString("fileName")
                    if (id.isNotBlank() && fileName.isNotBlank()) {
                        add(SavedTrack(id = id, title = title.ifBlank { "Lagu MP3" }, fileName = fileName))
                    }
                }
            }
        } catch (_: Exception) {
            emptyList()
        }
    }

    private fun resolveDisplayName(uri: Uri): String {
        try {
            appContext.contentResolver.query(
                uri,
                arrayOf(OpenableColumns.DISPLAY_NAME),
                null,
                null,
                null
            )?.use { cursor ->
                if (cursor.moveToFirst()) {
                    val idx = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    if (idx >= 0) {
                        val name = cursor.getString(idx)
                        if (!name.isNullOrBlank()) return name
                    }
                }
            }
        } catch (_: Exception) {
        }
        return uri.lastPathSegment?.substringAfterLast('/') ?: "Lagu.mp3"
    }
}
