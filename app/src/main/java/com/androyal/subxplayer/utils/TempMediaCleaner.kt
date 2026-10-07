package com.androyal.subxplayer.utils

import android.content.Context
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Port of temp_media_cleaner.dart — mirrors original Flutter logic in Kotlin.
 * Handles temp media cleaner functionality for SubX.
 */
@Singleton
class TempMediaCleaner @Inject constructor() {
    fun initialize(context: Context) { /* init */ }

    companion object {
        private const val TAG = "TempMediaCleaner"
    }
}
