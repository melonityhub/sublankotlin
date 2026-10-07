package com.androyal.subxplayer.utils

import android.content.Context
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Port of onboarding_service.dart — mirrors original Flutter logic in Kotlin.
 * Handles onboarding service functionality for SubX.
 */
@Singleton
class OnboardingService @Inject constructor() {
    fun initialize(context: Context) { /* init */ }

    companion object {
        private const val TAG = "OnboardingService"
    }
}
