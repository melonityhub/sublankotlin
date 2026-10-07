package com.androyal.subxplayer.data.database.tables
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "medialibraryindex")
data class MediaLibraryIndex(
@PrimaryKey(autoGenerate = true)
    val id: Long,
    val path: String,
    val displayName: String,
    val mimeType: String,
    val sizeBytes: Long,
    val durationMs: Long,
    val dateAdded: Long,
    val folderPath: String,
)
