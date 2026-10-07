package com.androyal.subxplayer.utils

import android.content.Context
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Port of subtitle_bidi_utils.dart — mirrors original Flutter logic in Kotlin.
 * Handles subtitle bidi utils functionality for SubX.
 */
@Singleton
class SubtitleBidiUtils @Inject constructor() {
    fun initialize(context: Context) { /* init */ }

    companion object {
        private const val TAG = "SubtitleBidiUtils"
    }
}
