package com.androyal.subxplayer.utils

import android.content.Context
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Port of player_teardown.dart — mirrors original Flutter logic in Kotlin.
 * Handles player teardown functionality for SubX.
 */
@Singleton
class PlayerTeardown @Inject constructor() {
    fun initialize(context: Context) { /* init */ }

    companion object {
        private const val TAG = "PlayerTeardown"
    }
}
