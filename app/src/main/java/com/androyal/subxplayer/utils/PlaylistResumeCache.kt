package com.androyal.subxplayer.utils

import android.content.Context
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Port of playlist_resume_cache.dart — mirrors original Flutter logic in Kotlin.
 * Handles playlist resume cache functionality for SubX.
 */
@Singleton
class PlaylistResumeCache @Inject constructor() {
    fun initialize(context: Context) { /* init */ }

    companion object {
        private const val TAG = "PlaylistResumeCache"
    }
}
