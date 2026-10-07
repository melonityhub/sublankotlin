
package com.androyal.subxplayer.data.repository

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.androyal.subxplayer.MainViewModel.ThemeMode
import com.androyal.subxplayer.data.models.*
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore("subx_prefs")

@Singleton
class SettingsRepository @Inject constructor(@ApplicationContext private val context: Context) {
    private val json = Json { ignoreUnknownKeys = true }

    object Keys {
        val THEME_MODE = stringPreferencesKey("theme_mode")
        val PRIMARY_LANG = stringPreferencesKey("primary_lang")
        val SECONDARY_LANG = stringPreferencesKey("secondary_lang")
        val TRANSLATION_TARGET = stringPreferencesKey("translation_target")
        val TRANSLATION_SOURCE = stringPreferencesKey("translation_source")
        val AUTO_PIP = booleanPreferencesKey("auto_pip")
        val HARDWARE_DECODING = booleanPreferencesKey("hw_decoding")
        val SEEK_DURATION = intPreferencesKey("seek_duration")
        val DEFAULT_SPEED = floatPreferencesKey("default_speed")
        val PLAYER_SETTINGS = stringPreferencesKey("player_settings_json")
        val GESTURE_SETTINGS = stringPreferencesKey("gesture_settings_json")
        val SUBTITLE_SETTINGS = stringPreferencesKey("subtitle_settings_json")
        val TRANSLATION_SETTINGS = stringPreferencesKey("translation_settings_json")
        val PREMIUM_SETTINGS = stringPreferencesKey("premium_settings_json")
        val CATALOG_BASE_URL = stringPreferencesKey("catalog_base_url")
        val REVENUECAT_KEY = stringPreferencesKey("revenuecat_key")
        val FIREBASE_API_KEY = stringPreferencesKey("firebase_api_key")
        val SUBS_BASE_URL = stringPreferencesKey("subs_base_url")
        val ONBOARDING_DONE = booleanPreferencesKey("onboarding_done")
        val LAST_PLAYED_ID = stringPreferencesKey("last_played_id")
    }

    val themeModeFlow: Flow<ThemeMode> = context.dataStore.data.map {
        when (it[Keys.THEME_MODE]) { "LIGHT"->ThemeMode.LIGHT; "DARK"->ThemeMode.DARK; else->ThemeMode.SYSTEM }
    }
    val autoPipFlow: Flow<Boolean> = context.dataStore.data.map { it[Keys.AUTO_PIP] ?: true }
    val primaryLangFlow: Flow<String> = context.dataStore.data.map { it[Keys.PRIMARY_LANG] ?: "en" }
    val secondaryLangFlow: Flow<String?> = context.dataStore.data.map { it[Keys.SECONDARY_LANG] }
    val translationTargetFlow: Flow<String> = context.dataStore.data.map { it[Keys.TRANSLATION_TARGET] ?: "fa" }

    suspend fun setThemeMode(m: ThemeMode) { context.dataStore.edit { it[Keys.THEME_MODE]=m.name } }
    suspend fun setAutoPip(v:Boolean){ context.dataStore.edit{it[Keys.AUTO_PIP]=v} }
    suspend fun setPrimaryLang(c:String){ context.dataStore.edit{it[Keys.PRIMARY_LANG]=c} }
    suspend fun setSecondaryLang(c:String?){
        context.dataStore.edit{ if(c==null) it.remove(Keys.SECONDARY_LANG) else it[Keys.SECONDARY_LANG]=c }
    }
    suspend fun setTranslationTarget(c:String){ context.dataStore.edit{it[Keys.TRANSLATION_TARGET]=c} }
    suspend fun setOnboardingDone(v:Boolean){ context.dataStore.edit{it[Keys.ONBOARDING_DONE]=v} }
    val onboardingDoneFlow: Flow<Boolean> = context.dataStore.data.map{it[Keys.ONBOARDING_DONE]?:false}

    // Generic JSON prefs
    suspend fun savePlayerSettings(s: PlayerSettings){ context.dataStore.edit{it[Keys.PLAYER_SETTINGS]=json.encodeToString(s)}}
    suspend fun saveGestureSettings(s: GestureSettings){ context.dataStore.edit{it[Keys.GESTURE_SETTINGS]=json.encodeToString(s)}}
    suspend fun saveSubtitleSettings(s: SubtitleSettings){ context.dataStore.edit{it[Keys.SUBTITLE_SETTINGS]=json.encodeToString(s)}}

    // Secrets editable in Settings > Advanced > Secrets
    val catalogBaseUrlFlow: Flow<String> = context.dataStore.data.map{it[Keys.CATALOG_BASE_URL] ?: AppConfig().catalogBaseUrl}
    val revenueCatKeyFlow: Flow<String> = context.dataStore.data.map{it[Keys.REVENUECAT_KEY] ?: AppConfig().revenueCatGoogleKey}
    val subsBaseUrlFlow: Flow<String> = context.dataStore.data.map{it[Keys.SUBS_BASE_URL] ?: AppConfig().subsBaseUrl}
    suspend fun setCatalogBaseUrl(v:String){ context.dataStore.edit{it[Keys.CATALOG_BASE_URL]=v}}
    suspend fun setRevenueCatKey(v:String){ context.dataStore.edit{it[Keys.REVENUECAT_KEY]=v}}
    suspend fun setFirebaseApiKey(v:String){ context.dataStore.edit{it[Keys.FIREBASE_API_KEY]=v}}
    suspend fun setSubsBaseUrl(v:String){ context.dataStore.edit{it[Keys.SUBS_BASE_URL]=v}}
    suspend fun setLastPlayedId(id:String){ context.dataStore.edit{it[Keys.LAST_PLAYED_ID]=id}}
    val lastPlayedIdFlow: Flow<String?> = context.dataStore.data.map{it[Keys.LAST_PLAYED_ID]}
}
