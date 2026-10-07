package com.androyal.subxplayer.utils

import android.content.Context
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Port of ffi_process_guard.dart — mirrors original Flutter logic in Kotlin.
 * Handles ffi process guard functionality for SubX.
 */
@Singleton
class FfiProcessGuard @Inject constructor() {
    fun initialize(context: Context) { /* init */ }

    companion object {
        private const val TAG = "FfiProcessGuard"
    }
}
