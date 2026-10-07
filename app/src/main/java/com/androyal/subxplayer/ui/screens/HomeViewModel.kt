package com.androyal.subxplayer.ui.screens

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androyal.subxplayer.data.database.daos.MediaLibraryIndexDao
import com.androyal.subxplayer.data.models.FolderEntry
import com.androyal.subxplayer.data.models.VideoItem
import com.androyal.subxplayer.utils.AppLogger
import com.androyal.subxplayer.utils.PermissionHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

data class HomeUiState(
    val isLoading: Boolean = false,
    val hasPermission: Boolean = true,
    val videos: List<VideoItem> = emptyList(),
    val folders: List<FolderEntry> = emptyList(),
    val breadcrumb: List<String> = emptyList(),
    val searchQuery: String = "",
    val error: String? = null,
    val currentPath: String? = null
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val mediaDao: MediaLibraryIndexDao
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState(isLoading = true, hasPermission = PermissionHelper.hasMediaPermissions(context)))
    val uiState: StateFlow<HomeUiState> = _uiState

    // Keep full list for filtering
    private var allVideos: List<VideoItem> = emptyList()

    init { refresh() }

    fun refresh() {
        viewModelScope.launch(Dispatchers.IO) {
            val hasPerm = PermissionHelper.hasMediaPermissions(context)
            _uiState.value = _uiState.value.copy(isLoading = true, hasPermission = hasPerm, error = null)
            if (!hasPerm) {
                _uiState.value = _uiState.value.copy(isLoading = false)
                return@launch
            }
            try {
                val videos = queryVideos(_uiState.value.currentPath)
                allVideos = videos
                val filtered = applySearch(videos, _uiState.value.searchQuery)
                val folders = groupFolders(videos)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    videos = filtered,
                    folders = if (_uiState.value.searchQuery.isBlank()) folders else emptyList(),
                    error = if (videos.isEmpty()) null else null
                )
                AppLogger.d("Loaded ${videos.size} videos")
            } catch (e: Exception) {
                AppLogger.w("loadMedia failed", e)
                _uiState.value = _uiState.value.copy(isLoading = false, error = e.message)
            }
        }
    }

    fun onPermissionResult(granted: Boolean) {
        _uiState.value = _uiState.value.copy(hasPermission = granted)
        if (granted) refresh()
    }

    fun loadMedia(path: String? = null) {
        _uiState.value = _uiState.value.copy(currentPath = path, breadcrumb = path?.split("/")?.filter { it.isNotEmpty() } ?: emptyList())
        refresh()
    }

    private suspend fun queryVideos(folderPath: String?): List<VideoItem> = withContext(Dispatchers.IO) {
        val list = mutableListOf<VideoItem>()
        try {
            // Query videos
            queryMediaStore(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, list, folderPath)
            // Also query audio (for MP3, WAV, FLAC etc)
            queryMediaStore(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, list, folderPath, isAudio = true)
            // Also include Documents via MediaStore.Files for MKV/AVI/MOV etc that may not be indexed as video
            queryGenericFiles(list, folderPath)
        } catch (e: Exception) {
            AppLogger.w("queryVideos error", e)
        }
        // Deduplicate by uri
        list.distinctBy { it.uri }.sortedByDescending { it.dateAdded }
    }

    private fun queryMediaStore(uri: Uri, out: MutableList<VideoItem>, folderPath: String?, isAudio: Boolean = false) {
        val projection = arrayOf(
            MediaStore.MediaColumns._ID,
            MediaStore.MediaColumns.DISPLAY_NAME,
            MediaStore.MediaColumns.SIZE,
            MediaStore.MediaColumns.MIME_TYPE,
            MediaStore.MediaColumns.DATA,
            MediaStore.MediaColumns.DATE_ADDED,
            MediaStore.Video.Media.DURATION,
            MediaStore.Video.Media.WIDTH,
            MediaStore.Video.Media.HEIGHT
        )
        // Filter by folder if specified
        val sel = if (folderPath != null) "${MediaStore.MediaColumns.DATA} LIKE ?" else null
        val args = if (folderPath != null) arrayOf("$folderPath%") else null
        try {
            context.contentResolver.query(uri, projection, sel, args, "${MediaStore.MediaColumns.DATE_ADDED} DESC")?.use { c ->
                val idIdx = c.getColumnIndexOrThrow(MediaStore.MediaColumns._ID)
                val nameIdx = c.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME)
                val sizeIdx = c.getColumnIndex(MediaStore.MediaColumns.SIZE)
                val mimeIdx = c.getColumnIndex(MediaStore.MediaColumns.MIME_TYPE)
                val dataIdx = c.getColumnIndex(MediaStore.MediaColumns.DATA)
                val dateIdx = c.getColumnIndex(MediaStore.MediaColumns.DATE_ADDED)
                val durIdx = c.getColumnIndex(MediaStore.Video.Media.DURATION)
                val wIdx = c.getColumnIndex(MediaStore.Video.Media.WIDTH)
                val hIdx = c.getColumnIndex(MediaStore.Video.Media.HEIGHT)
                while (c.moveToNext()) {
                    try {
                        val id = c.getLong(idIdx)
                        val name = c.getString(nameIdx) ?: continue
                        // Filter supported extensions early
                        if (!isSupported(name)) continue
                        val dataPath = if (dataIdx >= 0) c.getString(dataIdx) ?: "" else ""
                        if (folderPath != null && !dataPath.startsWith(folderPath)) continue
                        val contentUri = Uri.withAppendedPath(uri, id.toString()).toString()
                        out.add(
                            VideoItem(
                                id = id.toString(),
                                uri = contentUri,
                                displayName = name,
                                durationMs = if (durIdx >= 0) c.getLong(durIdx) else 0L,
                                sizeBytes = if (sizeIdx >= 0) c.getLong(sizeIdx) else 0L,
                                width = if (wIdx >= 0) c.getInt(wIdx) else 0,
                                height = if (hIdx >= 0) c.getInt(hIdx) else 0,
                                mimeType = if (mimeIdx >= 0) c.getString(mimeIdx) else null,
                                folderPath = dataPath.substringBeforeLast("/"),
                                dateAdded = if (dateIdx >= 0) c.getLong(dateIdx) else 0L
                            )
                        )
                    } catch (_: Exception) { continue }
                }
            }
        } catch (e: Exception) {
            AppLogger.w("queryMediaStore $uri failed", e)
        }
    }

    private fun queryGenericFiles(out: MutableList<VideoItem>, folderPath: String?) {
        // Fallback for files not indexed: scan via MediaStore.Files for video/audio mime + extension
        val uri = MediaStore.Files.getContentUri("external")
        val proj = arrayOf(
            MediaStore.Files.FileColumns._ID,
            MediaStore.Files.FileColumns.DISPLAY_NAME,
            MediaStore.Files.FileColumns.MIME_TYPE,
            MediaStore.Files.FileColumns.SIZE,
            MediaStore.Files.FileColumns.DATA,
            MediaStore.Files.FileColumns.DATE_ADDED
        )
        val sel = "(${MediaStore.Files.FileColumns.MEDIA_TYPE}=${MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO} OR ${MediaStore.Files.FileColumns.MEDIA_TYPE}=${MediaStore.Files.FileColumns.MEDIA_TYPE_AUDIO})" +
                if (folderPath != null) " AND ${MediaStore.Files.FileColumns.DATA} LIKE ?" else ""
        val args = if (folderPath != null) arrayOf("$folderPath%") else null
        // Already covered by previous queries, so this is just a safety duplicate - skip if we already have results
        if (out.size > 50) return
        try {
            context.contentResolver.query(uri, proj, sel, args, null)?.use { c ->
                val idIdx = c.getColumnIndexOrThrow(MediaStore.Files.FileColumns._ID)
                val nameIdx = c.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DISPLAY_NAME)
                val mimeIdx = c.getColumnIndex(MediaStore.Files.FileColumns.MIME_TYPE)
                val sizeIdx = c.getColumnIndex(MediaStore.Files.FileColumns.SIZE)
                val dataIdx = c.getColumnIndex(MediaStore.Files.FileColumns.DATA)
                val dateIdx = c.getColumnIndex(MediaStore.Files.FileColumns.DATE_ADDED)
                while (c.moveToNext()) {
                    try {
                        val name = c.getString(nameIdx) ?: continue
                        if (!isSupported(name)) continue
                        if (out.any { it.displayName == name }) continue
                        val id = c.getLong(idIdx)
                        val dataPath = c.getString(dataIdx) ?: ""
                        if (folderPath != null && !dataPath.startsWith(folderPath)) continue
                        val contentUri = Uri.withAppendedPath(uri, id.toString()).toString()
                        out.add(
                            VideoItem(
                                id = "f_$id",
                                uri = contentUri,
                                displayName = name,
                                sizeBytes = c.getLong(sizeIdx),
                                mimeType = c.getString(mimeIdx),
                                folderPath = dataPath.substringBeforeLast("/"),
                                dateAdded = c.getLong(dateIdx)
                            )
                        )
                    } catch (_: Exception) { continue }
                }
            }
        } catch (_: Exception) {}
    }

    private fun isSupported(name: String): Boolean {
        val lower = name.lowercase()
        return lower.endsWith(".mp4") || lower.endsWith(".mkv") || lower.endsWith(".avi") ||
                lower.endsWith(".mov") || lower.endsWith(".webm") || lower.endsWith(".m4v") ||
                lower.endsWith(".3gp") || lower.endsWith(".ts") || lower.endsWith(".m3u") ||
                lower.endsWith(".m3u8") || lower.endsWith(".mpd") || lower.endsWith(".mp3") ||
                lower.endsWith(".wav") || lower.endsWith(".flac") || lower.endsWith(".aac") ||
                lower.endsWith(".ogg") || lower.endsWith(".wma") || lower.endsWith(".opus")
    }

    private fun groupFolders(videos: List<VideoItem>): List<FolderEntry> {
        return videos.groupBy { it.folderPath }.map { (path, vs) ->
            FolderEntry(path = path, name = path.substringAfterLast("/").ifEmpty { "Root" }, videoCount = vs.size, totalDurationMs = vs.sumOf { it.durationMs })
        }.sortedBy { it.name.lowercase() }
    }

    private fun applySearch(videos: List<VideoItem>, q: String): List<VideoItem> {
        if (q.isBlank()) return videos
        val lower = q.lowercase()
        return videos.filter { it.displayName.lowercase().contains(lower) || it.folderPath.lowercase().contains(lower) }
    }

    fun openFolder(entry: FolderEntry) { loadMedia(entry.path) }

    fun playFolder(entry: FolderEntry) { /* handled via navigation to player with playlist */ }

    fun navigateToBreadcrumb(index: Int) {
        val path = if (index < 0) null else _uiState.value.breadcrumb.take(index + 1).joinToString("/", prefix = "/")
        loadMedia(if (index < 0) null else path)
    }

    fun onSearch(q: String) {
        _uiState.value = _uiState.value.copy(searchQuery = q)
        val filtered = applySearch(allVideos, q)
        _uiState.value = _uiState.value.copy(
            videos = filtered,
            folders = if (q.isBlank()) groupFolders(allVideos) else emptyList()
        )
    }

    fun onPickedUri(uri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Persist permission
                try {
                    context.contentResolver.takePersistableUriPermission(uri, android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION)
                } catch (_: Exception) {}
                val name = queryDisplayName(uri) ?: uri.lastPathSegment ?: "video"
                val item = VideoItem(id = uri.toString(), uri = uri.toString(), displayName = name, mimeType = context.contentResolver.getType(uri))
                withContext(Dispatchers.Main) {
                    // Prepend to list so it appears immediately
                    allVideos = listOf(item) + allVideos
                    val filtered = applySearch(allVideos, _uiState.value.searchQuery)
                    _uiState.value = _uiState.value.copy(videos = filtered)
                }
                AppLogger.d("Picked uri: $uri name=$name")
            } catch (e: Exception) {
                AppLogger.w("onPickedUri failed", e)
            }
        }
    }

    private fun queryDisplayName(uri: Uri): String? {
        return try {
            context.contentResolver.query(uri, arrayOf(MediaStore.MediaColumns.DISPLAY_NAME), null, null, null)?.use { c ->
                if (c.moveToFirst()) c.getString(0) else null
            }
        } catch (_: Exception) { null }
    }

    fun clearError() { _uiState.value = _uiState.value.copy(error = null) }
}
