
package com.androyal.subxplayer.data.repository

import com.androyal.subxplayer.data.database.daos.TranslationCacheDao
import com.androyal.subxplayer.data.database.tables.TranslationCache
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TranslationCacheRepository @Inject constructor(private val dao: TranslationCacheDao) {
    suspend fun getCached(source:String, srcLang:String, tgtLang:String): String? {
        return dao.getAll().firstOrNull{it.sourceText==source && it.sourceLang==srcLang && it.targetLang==tgtLang}?.translatedText
    }
    suspend fun put(source:String, srcLang:String, tgtLang:String, translated:String){
        dao.insert(TranslationCache(0, source, srcLang, tgtLang, translated, System.currentTimeMillis()))
    }
}
