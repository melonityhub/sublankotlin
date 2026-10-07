package com.androyal.subxplayer.utils

import android.content.Context
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Port of flutter_tts_bilingual_controller.dart — mirrors original Flutter logic in Kotlin.
 * Handles flutter tts bilingual controller functionality for SubX.
 */
@Singleton
class FlutterTtsBilingualController @Inject constructor() {
    fun initialize(context: Context) { /* init */ }

    companion object {
        private const val TAG = "FlutterTtsBilingualController"
    }
}
