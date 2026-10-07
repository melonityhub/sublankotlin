
package com.androyal.subxplayer.data.models

import kotlinx.serialization.Serializable

@Serializable
data class SubtitleCue(
    val id: Long = 0,
    val startMs: Long,
    val endMs: Long,
    val text: String,
    val originalText: String = text,
    val translatedText: String? = null,
    val isBookmarked: Boolean = false,
    val language: String = "en"
) {
    val durationMs: Long get() = endMs - startMs
    val displayText: String get() = translatedText ?: text
}

data class SubtitleCueUiModel(
    val cue: SubtitleCue,
    val isActive: Boolean = false,
    val isHighlighted: Boolean = false,
    val searchQuery: String? = null
)

data class SubtitleSettings(
    val primaryLanguage: String = "en",
    val secondaryLanguage: String? = null,
    val fontSizeSp: Float = 18f,
    val textColor: Long = 0xFFFFFFFF,
    val backgroundColor: Long = 0x80000000,
    val outlineColor: Long = 0xFF000000,
    val outlineWidth: Float = 2f,
    val position: SubtitlePosition = SubtitlePosition.BOTTOM,
    val secondaryEnabled: Boolean = false,
    val selectableText: Boolean = true
)

enum class SubtitlePosition { TOP, CENTER, BOTTOM, CUSTOM }

data class CachedSubtitleInfo(
    val videoId: String,
    val path: String,
    val language: String,
    val source: String,
    val isEmbedded: Boolean = false
)

data class CachedSubtitleUrl(val url: String, val language: String)
