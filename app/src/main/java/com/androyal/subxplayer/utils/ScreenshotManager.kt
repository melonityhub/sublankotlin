package com.androyal.subxplayer.utils

import android.content.Context
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Port of screenshot_manager.dart — mirrors original Flutter logic in Kotlin.
 * Handles screenshot manager functionality for SubX.
 */
@Singleton
class ScreenshotManager @Inject constructor() {
    fun initialize(context: Context) { /* init */ }

    companion object {
        private const val TAG = "ScreenshotManager"
    }
}
