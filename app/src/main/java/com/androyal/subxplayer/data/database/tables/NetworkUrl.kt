package com.androyal.subxplayer.data.database.tables
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "networkurl")
data class NetworkUrl(
@PrimaryKey(autoGenerate = true)
    val id: Long
    val url: String
    val title: String
    val addedAt: Long
)
