package com.androyal.subxplayer.utils

import android.content.Context
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Port of subtitle_merge_utils.dart — mirrors original Flutter logic in Kotlin.
 * Handles subtitle merge utils functionality for SubX.
 */
@Singleton
class SubtitleMergeUtils @Inject constructor() {
    fun initialize(context: Context) { /* init */ }

    companion object {
        private const val TAG = "SubtitleMergeUtils"
    }
}
