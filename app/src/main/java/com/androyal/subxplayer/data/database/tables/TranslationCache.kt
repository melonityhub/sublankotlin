package com.androyal.subxplayer.data.database.tables
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "translationcache")
data class TranslationCache(
@PrimaryKey(autoGenerate = true)
    val id: Long,
    val sourceText: String,
    val sourceLang: String,
    val targetLang: String,
    val translatedText: String,
    val createdAt: Long,
)
