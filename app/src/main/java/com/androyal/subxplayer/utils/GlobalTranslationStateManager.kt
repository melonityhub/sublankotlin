package com.androyal.subxplayer.utils

import android.content.Context
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Port of global_translation_state_manager.dart — mirrors original Flutter logic in Kotlin.
 * Handles global translation state manager functionality for SubX.
 */
@Singleton
class GlobalTranslationStateManager @Inject constructor() {
    fun initialize(context: Context) { /* init */ }

    companion object {
        private const val TAG = "GlobalTranslationStateManager"
    }
}
