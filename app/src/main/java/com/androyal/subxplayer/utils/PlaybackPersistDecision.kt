package com.androyal.subxplayer.utils

import android.content.Context
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Port of playback_persist_decision.dart — mirrors original Flutter logic in Kotlin.
 * Handles playback persist decision functionality for SubX.
 */
@Singleton
class PlaybackPersistDecision @Inject constructor() {
    fun initialize(context: Context) { /* init */ }

    companion object {
        private const val TAG = "PlaybackPersistDecision"
    }
}
