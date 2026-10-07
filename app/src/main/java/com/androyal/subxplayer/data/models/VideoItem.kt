
package com.androyal.subxplayer.data.models

import android.net.Uri
import kotlinx.serialization.Serializable

@Serializable
data class VideoItem(
    val id: String,
    val uri: String,
    val displayName: String,
    val durationMs: Long = 0L,
    val sizeBytes: Long = 0L,
    val width: Int = 0,
    val height: Int = 0,
    val mimeType: String? = null,
    val folderPath: String = "",
    val dateAdded: Long = 0L,
    val dateModified: Long = 0L,
    val isFavorite: Boolean = false,
    val lastPositionMs: Long = 0L,
    val thumbnailPath: String? = null
) {
    val uriParsed: Uri get() = Uri.parse(uri)
    val isVideo: Boolean get() = mimeType?.startsWith("video") == true || uri.endsWith(".mp4", true) || uri.endsWith(".mkv", true)
    val isAudio: Boolean get() = mimeType?.startsWith("audio") == true
}

data class VideoMeta(
    val durationMs: Long,
    val width: Int,
    val height: Int,
    val rotation: Int,
    val bitrate: Long,
    val hasAudio: Boolean,
    val hasVideo: Boolean
)

data class FolderEntry(
    val path: String,
    val name: String,
    val videoCount: Int,
    val totalDurationMs: Long,
    val thumbnailUri: String? = null,
    val isFavorite: Boolean = false
)

enum class BrowserViewMode { LIST, GRID, TREE }
enum class SortOrder { NAME_ASC, NAME_DESC, DATE_DESC, DATE_ASC, SIZE_DESC, DURATION_DESC }
