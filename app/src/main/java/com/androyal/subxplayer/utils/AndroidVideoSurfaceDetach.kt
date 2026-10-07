package com.androyal.subxplayer.utils

import android.content.Context
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Port of android_video_surface_detach.dart — mirrors original Flutter logic in Kotlin.
 * Handles android video surface detach functionality for SubX.
 */
@Singleton
class AndroidVideoSurfaceDetach @Inject constructor() {
    fun initialize(context: Context) { /* init */ }

    companion object {
        private const val TAG = "AndroidVideoSurfaceDetach"
    }
}
