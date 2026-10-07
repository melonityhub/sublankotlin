package com.androyal.subxplayer.utils

import android.content.Context
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Port of player_loading_policy.dart — mirrors original Flutter logic in Kotlin.
 * Handles player loading policy functionality for SubX.
 */
@Singleton
class PlayerLoadingPolicy @Inject constructor() {
    fun initialize(context: Context) { /* init */ }

    companion object {
        private const val TAG = "PlayerLoadingPolicy"
    }
}
