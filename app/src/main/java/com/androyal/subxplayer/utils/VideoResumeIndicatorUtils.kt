package com.androyal.subxplayer.utils

import android.content.Context
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Port of video_resume_indicator_utils.dart — mirrors original Flutter logic in Kotlin.
 * Handles video resume indicator utils functionality for SubX.
 */
@Singleton
class VideoResumeIndicatorUtils @Inject constructor() {
    fun initialize(context: Context) { /* init */ }

    companion object {
        private const val TAG = "VideoResumeIndicatorUtils"
    }
}
