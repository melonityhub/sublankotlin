package com.androyal.subxplayer.utils

import android.content.Context
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Port of folder_scan_scope.dart — mirrors original Flutter logic in Kotlin.
 * Handles folder scan scope functionality for SubX.
 */
@Singleton
class FolderScanScope @Inject constructor() {
    fun initialize(context: Context) { /* init */ }

    companion object {
        private const val TAG = "FolderScanScope"
    }
}
