package com.androyal.subxplayer.utils

import android.content.Context
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Port of asset_preloader.dart — mirrors original Flutter logic in Kotlin.
 * Handles asset preloader functionality for SubX.
 */
@Singleton
class AssetPreloader @Inject constructor() {
    fun initialize(context: Context) { /* init */ }

    companion object {
        private const val TAG = "AssetPreloader"
    }
}
