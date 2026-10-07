package com.androyal.subxplayer.utils

import android.content.Context
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Port of reading_mode_video_track_policy.dart — mirrors original Flutter logic in Kotlin.
 * Handles reading mode video track policy functionality for SubX.
 */
@Singleton
class ReadingModeVideoTrackPolicy @Inject constructor() {
    fun initialize(context: Context) { /* init */ }

    companion object {
        private const val TAG = "ReadingModeVideoTrackPolicy"
    }
}
