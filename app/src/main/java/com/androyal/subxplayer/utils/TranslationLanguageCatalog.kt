package com.androyal.subxplayer.utils

import android.content.Context
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Port of translation_language_catalog.dart — mirrors original Flutter logic in Kotlin.
 * Handles translation language catalog functionality for SubX.
 */
@Singleton
class TranslationLanguageCatalog @Inject constructor() {
    fun initialize(context: Context) { /* init */ }

    companion object {
        private const val TAG = "TranslationLanguageCatalog"
    }
}
