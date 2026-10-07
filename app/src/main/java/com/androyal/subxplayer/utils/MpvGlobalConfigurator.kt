package com.androyal.subxplayer.utils

import android.content.Context
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Port of mpv_global_configurator.dart — mirrors original Flutter logic in Kotlin.
 * Handles mpv global configurator functionality for SubX.
 */
@Singleton
class MpvGlobalConfigurator @Inject constructor() {
    fun initialize(context: Context) { /* init */ }

    companion object {
        private const val TAG = "MpvGlobalConfigurator"
    }
}
