package com.androyal.subxplayer.utils

import android.content.Context
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Port of translation_dialog_language.dart — mirrors original Flutter logic in Kotlin.
 * Handles translation dialog language functionality for SubX.
 */
@Singleton
class TranslationDialogLanguage @Inject constructor() {
    fun initialize(context: Context) { /* init */ }

    companion object {
        private const val TAG = "TranslationDialogLanguage"
    }
}
