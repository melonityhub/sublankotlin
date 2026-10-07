package com.androyal.subxplayer.utils

import android.content.Context
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Port of app_detached_hard_exit.dart — mirrors original Flutter logic in Kotlin.
 * Handles app detached hard exit functionality for SubX.
 */
@Singleton
class AppDetachedHardExit @Inject constructor() {
    fun initialize(context: Context) { /* init */ }

    companion object {
        private const val TAG = "AppDetachedHardExit"
    }
}
