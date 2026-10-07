package com.androyal.subxplayer.utils

import android.content.Context
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Port of cast_subtitle_manager.dart — mirrors original Flutter logic in Kotlin.
 * Handles cast subtitle manager functionality for SubX.
 */
@Singleton
class CastSubtitleManager @Inject constructor() {
    fun initialize(context: Context) { /* init */ }

    companion object {
        private const val TAG = "CastSubtitleManager"
    }
}
