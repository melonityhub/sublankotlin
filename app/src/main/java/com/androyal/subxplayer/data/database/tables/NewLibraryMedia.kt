package com.androyal.subxplayer.data.database.tables
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "newlibrarymedia")
data class NewLibraryMedia(
@PrimaryKey(autoGenerate = true)
    val id: Long
    val uri: String
    val displayName: String
    val folderPath: String
    val durationMs: Long
    val lastPositionMs: Long
    val isFavorite: Boolean
    val dateAdded: Long
)
