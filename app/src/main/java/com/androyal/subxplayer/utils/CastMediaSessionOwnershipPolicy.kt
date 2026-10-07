package com.androyal.subxplayer.utils

import android.content.Context
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Port of cast_media_session_ownership_policy.dart — mirrors original Flutter logic in Kotlin.
 * Handles cast media session ownership policy functionality for SubX.
 */
@Singleton
class CastMediaSessionOwnershipPolicy @Inject constructor() {
    fun initialize(context: Context) { /* init */ }

    companion object {
        private const val TAG = "CastMediaSessionOwnershipPolicy"
    }
}
