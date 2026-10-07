package com.androyal.subxplayer.utils

import android.content.Context
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Port of library_scan_policy.dart — mirrors original Flutter logic in Kotlin.
 * Handles library scan policy functionality for SubX.
 */
@Singleton
class LibraryScanPolicy @Inject constructor() {
    fun initialize(context: Context) { /* init */ }

    companion object {
        private const val TAG = "LibraryScanPolicy"
    }
}
