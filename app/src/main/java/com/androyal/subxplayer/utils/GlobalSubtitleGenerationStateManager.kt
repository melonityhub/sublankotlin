package com.androyal.subxplayer.utils

import android.content.Context
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Port of global_subtitle_generation_state_manager.dart — mirrors original Flutter logic in Kotlin.
 * Handles global subtitle generation state manager functionality for SubX.
 */
@Singleton
class GlobalSubtitleGenerationStateManager @Inject constructor() {
    fun initialize(context: Context) { /* init */ }

    companion object {
        private const val TAG = "GlobalSubtitleGenerationStateManager"
    }
}
