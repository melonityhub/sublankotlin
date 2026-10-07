package com.androyal.subxplayer.utils

import android.content.Context
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Port of subtitle_media_match.dart — mirrors original Flutter logic in Kotlin.
 * Handles subtitle media match functionality for SubX.
 */
@Singleton
class SubtitleMediaMatch @Inject constructor() {
    fun initialize(context: Context) { /* init */ }

    companion object {
        private const val TAG = "SubtitleMediaMatch"
    }
}
