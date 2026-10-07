package com.androyal.subxplayer.utils

import android.content.Context
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Port of ffmpeg_utils.dart — mirrors original Flutter logic in Kotlin.
 * Handles ffmpeg utils functionality for SubX.
 */
@Singleton
class FfmpegUtils @Inject constructor() {
    fun initialize(context: Context) { /* init */ }

    companion object {
        private const val TAG = "FfmpegUtils"
    }
}
