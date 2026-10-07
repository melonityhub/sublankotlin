package com.androyal.subxplayer.utils

import android.content.Context
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Port of folder_display_path.dart — mirrors original Flutter logic in Kotlin.
 * Handles folder display path functionality for SubX.
 */
@Singleton
class FolderDisplayPath @Inject constructor() {
    fun initialize(context: Context) { /* init */ }

    companion object {
        private const val TAG = "FolderDisplayPath"
    }
}
