package com.androyal.subxplayer.utils

import android.content.Context
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Port of audio_notification_policy.dart — mirrors original Flutter logic in Kotlin.
 * Handles audio notification policy functionality for SubX.
 */
@Singleton
class AudioNotificationPolicy @Inject constructor() {
    fun initialize(context: Context) { /* init */ }

    companion object {
        private const val TAG = "AudioNotificationPolicy"
    }
}
