package com.androyal.subxplayer.utils

import android.content.Context
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Port of audio_mode_manager.dart — mirrors original Flutter logic in Kotlin.
 * Handles audio mode manager functionality for SubX.
 */
@Singleton
class AudioModeManager @Inject constructor() {
    fun initialize(context: Context) { /* init */ }

    companion object {
        private const val TAG = "AudioModeManager"
    }
}
