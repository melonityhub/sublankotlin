
package com.androyal.subxplayer.data.models

import kotlinx.serialization.Serializable

@Serializable
data class AppConfig(
    val catalogBaseUrl: String = "https://catalog.subx.app",
    val wwwBaseUrl: String = "https://www.subx.app",
    val translationMirrorsUrl: String = "https://catalog.subx.app/config/translation_mirrors.json",
    val revenueCatGoogleKey: String = "goog_luBnwcCAtCYXvIihhKyRTXAmAxd",
    val revenueCatEntitlementId: String = "plus",
    val firebaseProjectId: String = "subx-video-player",
    val firebaseApiKey: String = "AIzaSyCt-RfF5oylNh4bm4MB1kjpzkJH77rPa-4",
    val firebaseAppId: String = "1:14794115470:android:2fc0ef1857ee876ab2bcdf",
    val firebaseSenderId: String = "14794115470",
    val firebaseStorageBucket: String = "subx-video-player.firebasestorage.app",
    val castAppId: String = "ED7DABE1",
    val subsBaseUrl: String = "https://www.subx.app/subs",
    val catalogReleaseUrl: String = "https://www.subx.app/catalog/release",
    val privacyUrl: String = "https://www.subx.app/privacy/"
)

object AppSecrets {
    // Overridable via Settings > Advanced > Secrets (DataStore)
    // If empty, falls back to defaults above
}
