package com.androyal.subxplayer.utils



object AnalyticsEvents {
    fun logVideoPlay(id:String, name:String){ try{ AppLogger.d("Analytics: play $name")}catch(_:Exception){} }
    fun logSubtitleGenerated(lang:String){ AppLogger.d("Analytics: generated $lang") }
    fun logTranslation(src:String, tgt:String){ AppLogger.d("Analytics: translate $src->$tgt") }
    fun logPremiumUpsellShown(feature:String){ AppLogger.d("Analytics: upsell $feature") }
    fun logAnkiExport(deck:String){ AppLogger.d("Analytics: anki $deck")}
}
