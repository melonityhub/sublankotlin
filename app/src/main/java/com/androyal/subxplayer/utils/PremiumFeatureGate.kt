package com.androyal.subxplayer.utils

object PremiumFeatureGate {
    fun isAllowed(feature:String, isPremium:Boolean): Boolean {
        val premiumOnly = setOf("auto_skip","bilingual_audio","anki_export","offline_translate","generate_subtitle")
        return if(feature in premiumOnly) isPremium else true
    }
}
