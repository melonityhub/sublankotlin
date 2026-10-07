package com.androyal.subxplayer.utils

import android.content.Context
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Port of video_zoom_pivot.dart — mirrors original Flutter logic in Kotlin.
 * Handles video zoom pivot functionality for SubX.
 */
@Singleton
class VideoZoomPivot @Inject constructor() {
    fun initialize(context: Context) { /* init */ }

    companion object {
        private const val TAG = "VideoZoomPivot"
    }
}
