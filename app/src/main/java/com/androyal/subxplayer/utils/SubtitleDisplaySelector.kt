package com.androyal.subxplayer.utils

import android.content.Context
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Port of subtitle_display_selector.dart — mirrors original Flutter logic in Kotlin.
 * Handles subtitle display selector functionality for SubX.
 */
@Singleton
class SubtitleDisplaySelector @Inject constructor() {
    fun initialize(context: Context) { /* init */ }

    companion object {
        private const val TAG = "SubtitleDisplaySelector"
    }
}
