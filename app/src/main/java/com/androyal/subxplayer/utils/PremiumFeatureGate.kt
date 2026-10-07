package com.androyal.subxplayer.utils

object PremiumFeatureGate {
    fun isAllowed(feature: String, isPremium: Boolean): Boolean = true // all unlocked
    fun isAllowed(feature: String): Boolean = true
}
