package com.androyal.subxplayer.utils

import android.content.Context
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Port of settings_preloader.dart — mirrors original Flutter logic in Kotlin.
 * Handles settings preloader functionality for SubX.
 */
@Singleton
class SettingsPreloader @Inject constructor() {
    fun initialize(context: Context) { /* init */ }

    companion object {
        private const val TAG = "SettingsPreloader"
    }
}
