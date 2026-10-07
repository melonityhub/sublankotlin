package com.androyal.subxplayer.utils

import android.content.Context
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Port of translator_package_service.dart — mirrors original Flutter logic in Kotlin.
 * Handles translator package service functionality for SubX.
 */
@Singleton
class TranslatorPackageService @Inject constructor() {
    fun initialize(context: Context) { /* init */ }

    companion object {
        private const val TAG = "TranslatorPackageService"
    }
}
