
package com.androyal.subxplayer.data.models

import kotlinx.serialization.Serializable

@Serializable
data class TranslationRequest(
    val text: String,
    val sourceLang: String,
    val targetLang: String
)

@Serializable
data class TranslationResponse(
    val translatedText: String,
    val sourceLang: String,
    val targetLang: String
)

data class TranslationSettings(
    val sourceLanguage: String = "en",
    val targetLanguage: String = "fa",
    val enableTranslation: Boolean = true,
    val autoTranslate: Boolean = false,
    val showOriginal: Boolean = true,
    val overlayStyle: Int = 0
)

data class LanguageModel(
    val code: String,
    val name: String,
    val nativeName: String,
    val flagRes: String? = null
)

data class PlayerSettings(
    val hardwareDecoding: Boolean = true,
    val seekDurationSec: Int = 10,
    val defaultSpeed: Float = 1f,
    val aspectRatio: String = "fit",
    val orientation: String = "auto",
    val autoPip: Boolean = true,
    val brightnessGesture: Boolean = true,
    val volumeGesture: Boolean = true,
    val seekGesture: Boolean = true,
    val doubleTapSeek: Int = 10,
    val showControlsOnStart: Boolean = true
)

data class GestureSettings(
    val panSeekEnabled: Boolean = true,
    val pinchZoomEnabled: Boolean = true,
    val doubleTapPlayPause: Boolean = true,
    val swipeBrightness: Boolean = true,
    val swipeVolume: Boolean = true,
    val longPressSpeedUp: Boolean = true,
    val speedUpFactor: Float = 2f
)

data class PremiumSettings(
    val isPremium: Boolean = false,
    val entitlementId: String = "plus",
    val expiryDate: Long? = null
)

data class EqualizerSettings(
    val enabled: Boolean = false,
    val preset: String = "Normal",
    val bassBoost: Int = 0,
    val bands: List<Float> = List(5) { 0f }
)

data class YoutubeCatalogVideo(
    val id: String,
    @com.google.gson.annotations.SerializedName("youtube_id") val youtubeId: String,
    val title: String,
    val channel: String,
    val duration: String,
    @com.google.gson.annotations.SerializedName("duration_sec") val durationSec: Int,
    val category: String,
    val language: String,
    val level: Int,
    @com.google.gson.annotations.SerializedName("subtitle_url") val subtitleUrl: String
)

data class CatalogMeta(
    val version: Int,
    val schemaVersion: Int,
    val status: Int,
    val updatedAt: String
)
