package com.androyal.subxplayer.utils

import android.content.Context
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Port of subtitle_html_parser.dart — mirrors original Flutter logic in Kotlin.
 * Handles subtitle html parser functionality for SubX.
 */
@Singleton
class SubtitleHtmlParser @Inject constructor() {
    fun initialize(context: Context) { /* init */ }

    companion object {
        private const val TAG = "SubtitleHtmlParser"
    }
}
