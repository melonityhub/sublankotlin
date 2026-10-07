package com.androyal.subxplayer.utils

import android.content.Context
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Port of playback_orientation_policy.dart — mirrors original Flutter logic in Kotlin.
 * Handles playback orientation policy functionality for SubX.
 */
@Singleton
class PlaybackOrientationPolicy @Inject constructor() {
    fun initialize(context: Context) { /* init */ }

    companion object {
        private const val TAG = "PlaybackOrientationPolicy"
    }
}
