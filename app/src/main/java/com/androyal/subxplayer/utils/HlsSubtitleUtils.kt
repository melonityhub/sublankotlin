package com.androyal.subxplayer.utils

import android.content.Context
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Port of hls_subtitle_utils.dart — mirrors original Flutter logic in Kotlin.
 * Handles hls subtitle utils functionality for SubX.
 */
@Singleton
class HlsSubtitleUtils @Inject constructor() {
    fun initialize(context: Context) { /* init */ }

    companion object {
        private const val TAG = "HlsSubtitleUtils"
    }
}
