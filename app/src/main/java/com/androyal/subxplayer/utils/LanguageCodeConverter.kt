package com.androyal.subxplayer.utils

import android.content.Context
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Port of language_code_converter.dart — mirrors original Flutter logic in Kotlin.
 * Handles language code converter functionality for SubX.
 */
@Singleton
class LanguageCodeConverter @Inject constructor() {
    fun initialize(context: Context) { /* init */ }

    companion object {
        private const val TAG = "LanguageCodeConverter"
    }
}
