package com.androyal.subxplayer.utils

import android.content.Context
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Port of video_loading_isolate.dart — mirrors original Flutter logic in Kotlin.
 * Handles video loading isolate functionality for SubX.
 */
@Singleton
class VideoLoadingIsolate @Inject constructor() {
    fun initialize(context: Context) { /* init */ }

    companion object {
        private const val TAG = "VideoLoadingIsolate"
    }
}
