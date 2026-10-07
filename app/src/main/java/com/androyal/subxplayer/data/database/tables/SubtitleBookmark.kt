package com.androyal.subxplayer.data.database.tables
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "subtitlebookmark")
data class SubtitleBookmark(
@PrimaryKey(autoGenerate = true)
    val id: Long,
    val videoId: String,
    val cueStartMs: Long,
    val cueEndMs: Long,
    val text: String,
    val translatedText: String,
    val createdAt: Long,
)
