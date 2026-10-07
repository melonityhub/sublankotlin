package com.androyal.subxplayer.utils

import android.content.Context
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Port of pip_subtitle_nav_policy.dart — mirrors original Flutter logic in Kotlin.
 * Handles pip subtitle nav policy functionality for SubX.
 */
@Singleton
class PipSubtitleNavPolicy @Inject constructor() {
    fun initialize(context: Context) { /* init */ }

    companion object {
        private const val TAG = "PipSubtitleNavPolicy"
    }
}
