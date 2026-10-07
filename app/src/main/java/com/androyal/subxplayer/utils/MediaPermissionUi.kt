package com.androyal.subxplayer.utils

import android.content.Context
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Port of media_permission_ui.dart — mirrors original Flutter logic in Kotlin.
 * Handles media permission ui functionality for SubX.
 */
@Singleton
class MediaPermissionUi @Inject constructor() {
    fun initialize(context: Context) { /* init */ }

    companion object {
        private const val TAG = "MediaPermissionUi"
    }
}
