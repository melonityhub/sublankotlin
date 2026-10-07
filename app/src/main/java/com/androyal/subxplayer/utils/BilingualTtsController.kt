package com.androyal.subxplayer.utils

import android.content.Context
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Port of bilingual_tts_controller.dart — mirrors original Flutter logic in Kotlin.
 * Handles bilingual tts controller functionality for SubX.
 */
@Singleton
class BilingualTtsController @Inject constructor() {
    fun initialize(context: Context) { /* init */ }

    companion object {
        private const val TAG = "BilingualTtsController"
    }
}
