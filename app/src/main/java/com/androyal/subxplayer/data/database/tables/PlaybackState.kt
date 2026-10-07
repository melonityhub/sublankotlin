package com.androyal.subxplayer.data.database.tables
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playbackstate")
data class PlaybackState(
@PrimaryKey(autoGenerate = true)
    val id: Long
    val videoId: String
    val positionMs: Long
    val durationMs: Long
    val lastPlayedAt: Long
    val playbackSpeed: Float
)
