package com.androyal.subxplayer.data.database.tables
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "videocache")
data class VideoCache(
@PrimaryKey(autoGenerate = true)
    val id: Long
    val videoId: String
    val cachePath: String
    val sizeBytes: Long
    val createdAt: Long
)
