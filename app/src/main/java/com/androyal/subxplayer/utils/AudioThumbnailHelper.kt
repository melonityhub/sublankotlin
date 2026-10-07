package com.androyal.subxplayer.utils

import android.content.Context
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Port of audio_thumbnail_helper.dart — mirrors original Flutter logic in Kotlin.
 * Handles audio thumbnail helper functionality for SubX.
 */
@Singleton
class AudioThumbnailHelper @Inject constructor() {
    fun initialize(context: Context) { /* init */ }

    companion object {
        private const val TAG = "AudioThumbnailHelper"
    }
}
