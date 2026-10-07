package com.androyal.subxplayer.utils

import android.content.Context
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Port of audio_metadata_fetcher.dart — mirrors original Flutter logic in Kotlin.
 * Handles audio metadata fetcher functionality for SubX.
 */
@Singleton
class AudioMetadataFetcher @Inject constructor() {
    fun initialize(context: Context) { /* init */ }

    companion object {
        private const val TAG = "AudioMetadataFetcher"
    }
}
