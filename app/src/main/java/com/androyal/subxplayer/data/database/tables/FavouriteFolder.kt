package com.androyal.subxplayer.data.database.tables
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favouritefolder")
data class FavouriteFolder(
@PrimaryKey(autoGenerate = true)
    val id: Long
    val name: String
    val path: String
    val createdAt: Long
)
