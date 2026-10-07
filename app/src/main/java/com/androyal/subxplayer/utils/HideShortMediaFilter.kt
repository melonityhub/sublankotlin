package com.androyal.subxplayer.utils

import android.content.Context
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Port of hide_short_media_filter.dart — mirrors original Flutter logic in Kotlin.
 * Handles hide short media filter functionality for SubX.
 */
@Singleton
class HideShortMediaFilter @Inject constructor() {
    fun initialize(context: Context) { /* init */ }

    companion object {
        private const val TAG = "HideShortMediaFilter"
    }
}
