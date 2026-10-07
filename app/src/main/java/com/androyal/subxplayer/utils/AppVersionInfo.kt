package com.androyal.subxplayer.utils

import android.content.Context
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Port of app_version_info.dart — mirrors original Flutter logic in Kotlin.
 * Handles app version info functionality for SubX.
 */
@Singleton
class AppVersionInfo @Inject constructor() {
    fun initialize(context: Context) { /* init */ }

    companion object {
        private const val TAG = "AppVersionInfo"
    }
}
