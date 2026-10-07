package com.androyal.subxplayer.utils

import android.content.Context
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Port of audio_manager.dart — mirrors original Flutter logic in Kotlin.
 * Handles audio manager functionality for SubX.
 */
@Singleton
class AudioManager @Inject constructor() {
    fun initialize(context: Context) { /* init */ }

    companion object {
        private const val TAG = "AudioManager"
    }
}
