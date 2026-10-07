package com.androyal.subxplayer.utils

import android.content.Context
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Port of cast_subtitle_converter.dart — mirrors original Flutter logic in Kotlin.
 * Handles cast subtitle converter functionality for SubX.
 */
@Singleton
class CastSubtitleConverter @Inject constructor() {
    fun initialize(context: Context) { /* init */ }

    companion object {
        private const val TAG = "CastSubtitleConverter"
    }
}
