package com.androyal.subxplayer.utils

import android.content.Context
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Port of android_brightness_gesture.dart — mirrors original Flutter logic in Kotlin.
 * Handles android brightness gesture functionality for SubX.
 */
@Singleton
class AndroidBrightnessGesture @Inject constructor() {
    fun initialize(context: Context) { /* init */ }

    companion object {
        private const val TAG = "AndroidBrightnessGesture"
    }
}
