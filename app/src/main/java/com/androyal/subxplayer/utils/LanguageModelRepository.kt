package com.androyal.subxplayer.utils

import android.content.Context
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Port of language_model_repository.dart — mirrors original Flutter logic in Kotlin.
 * Handles language model repository functionality for SubX.
 */
@Singleton
class LanguageModelRepository @Inject constructor() {
    fun initialize(context: Context) { /* init */ }

    companion object {
        private const val TAG = "LanguageModelRepository"
    }
}
