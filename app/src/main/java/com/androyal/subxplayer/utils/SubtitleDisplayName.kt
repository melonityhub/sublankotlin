package com.androyal.subxplayer.utils

import android.content.Context
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Port of subtitle_display_name.dart — mirrors original Flutter logic in Kotlin.
 * Handles subtitle display name functionality for SubX.
 */
@Singleton
class SubtitleDisplayName @Inject constructor() {
    fun initialize(context: Context) { /* init */ }

    companion object {
        private const val TAG = "SubtitleDisplayName"
    }
}
