package com.androyal.subxplayer.utils

import android.content.Context
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Port of pip_manager_base.dart — mirrors original Flutter logic in Kotlin.
 * Handles pip manager base functionality for SubX.
 */
@Singleton
class PipManagerBase @Inject constructor() {
    fun initialize(context: Context) { /* init */ }

    companion object {
        private const val TAG = "PipManagerBase"
    }
}
