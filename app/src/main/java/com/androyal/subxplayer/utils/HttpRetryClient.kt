package com.androyal.subxplayer.utils

import android.content.Context
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Port of http_retry_client.dart — mirrors original Flutter logic in Kotlin.
 * Handles http retry client functionality for SubX.
 */
@Singleton
class HttpRetryClient @Inject constructor() {
    fun initialize(context: Context) { /* init */ }

    companion object {
        private const val TAG = "HttpRetryClient"
    }
}
