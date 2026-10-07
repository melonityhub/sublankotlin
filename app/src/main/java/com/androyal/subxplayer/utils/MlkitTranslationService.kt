
package com.androyal.subxplayer.utils

import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import kotlinx.coroutines.tasks.await

object MlkitTranslationService {
    suspend fun translate(text:String, source:String, target:String): String {
        val src = TranslateLanguage.fromLanguageTag(source) ?: TranslateLanguage.ENGLISH
        val tgt = TranslateLanguage.fromLanguageTag(target) ?: TranslateLanguage.SPANISH
        val options = TranslatorOptions.Builder().setSourceLanguage(src).setTargetLanguage(tgt).build()
        val translator = Translation.getClient(options)
        try{
            translator.downloadModelIfNeeded().await()
            return translator.translate(text).await()
        } finally { translator.close() }
    }
}
