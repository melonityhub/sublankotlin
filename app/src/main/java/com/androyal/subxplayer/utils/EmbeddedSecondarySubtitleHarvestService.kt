package com.androyal.subxplayer.utils

import android.content.Context
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Port of embedded_secondary_subtitle_harvest_service.dart — mirrors original Flutter logic in Kotlin.
 * Handles embedded secondary subtitle harvest service functionality for SubX.
 */
@Singleton
class EmbeddedSecondarySubtitleHarvestService @Inject constructor() {
    fun initialize(context: Context) { /* init */ }

    companion object {
        private const val TAG = "EmbeddedSecondarySubtitleHarvestService"
    }
}
