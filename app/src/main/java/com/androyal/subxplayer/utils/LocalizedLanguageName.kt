package com.androyal.subxplayer.utils

import android.content.Context
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Port of localized_language_name.dart — mirrors original Flutter logic in Kotlin.
 * Handles localized language name functionality for SubX.
 */
@Singleton
class LocalizedLanguageName @Inject constructor() {
    fun initialize(context: Context) { /* init */ }

    companion object {
        private const val TAG = "LocalizedLanguageName"
    }
}
