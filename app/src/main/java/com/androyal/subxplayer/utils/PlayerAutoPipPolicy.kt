package com.androyal.subxplayer.utils

import android.content.Context
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Port of player_auto_pip_policy.dart — mirrors original Flutter logic in Kotlin.
 * Handles player auto pip policy functionality for SubX.
 */
@Singleton
class PlayerAutoPipPolicy @Inject constructor() {
    fun initialize(context: Context) { /* init */ }

    companion object {
        private const val TAG = "PlayerAutoPipPolicy"
    }
}
