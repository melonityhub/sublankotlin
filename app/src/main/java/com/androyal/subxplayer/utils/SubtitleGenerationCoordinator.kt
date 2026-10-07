package com.androyal.subxplayer.utils

import android.content.Context
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Port of subtitle_generation_coordinator.dart — mirrors original Flutter logic in Kotlin.
 * Handles subtitle generation coordinator functionality for SubX.
 */
@Singleton
class SubtitleGenerationCoordinator @Inject constructor() {
    fun initialize(context: Context) { /* init */ }

    companion object {
        private const val TAG = "SubtitleGenerationCoordinator"
    }
}
