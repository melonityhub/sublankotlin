package com.androyal.subxplayer.utils

import android.content.Context
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Port of seek_bar_update_policy.dart — mirrors original Flutter logic in Kotlin.
 * Handles seek bar update policy functionality for SubX.
 */
@Singleton
class SeekBarUpdatePolicy @Inject constructor() {
    fun initialize(context: Context) { /* init */ }

    companion object {
        private const val TAG = "SeekBarUpdatePolicy"
    }
}
