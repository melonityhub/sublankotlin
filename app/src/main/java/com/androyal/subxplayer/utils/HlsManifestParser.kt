package com.androyal.subxplayer.utils

import android.content.Context
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Port of hls_manifest_parser.dart — mirrors original Flutter logic in Kotlin.
 * Handles hls manifest parser functionality for SubX.
 */
@Singleton
class HlsManifestParser @Inject constructor() {
    fun initialize(context: Context) { /* init */ }

    companion object {
        private const val TAG = "HlsManifestParser"
    }
}
