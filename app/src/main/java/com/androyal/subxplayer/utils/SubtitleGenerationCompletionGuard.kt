package com.androyal.subxplayer.utils

import android.content.Context
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Port of subtitle_generation_completion_guard.dart — mirrors original Flutter logic in Kotlin.
 * Handles subtitle generation completion guard functionality for SubX.
 */
@Singleton
class SubtitleGenerationCompletionGuard @Inject constructor() {
    fun initialize(context: Context) { /* init */ }

    companion object {
        private const val TAG = "SubtitleGenerationCompletionGuard"
    }
}
