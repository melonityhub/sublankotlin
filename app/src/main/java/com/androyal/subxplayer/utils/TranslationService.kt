
package com.androyal.subxplayer.utils

import com.androyal.subxplayer.data.repository.TranslationCacheRepository
import com.androyal.subxplayer.network.TranslationApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TranslationService @Inject constructor(
    private val cache: TranslationCacheRepository
){
    // Primary uses MLKit, fallback to network mirrors
    suspend fun translate(text:String, source:String, target:String): String {
        cache.getCached(text, source, target)?.let{ return it }
        return try{
            val result = MlkitTranslationService.translate(text, source, target)
            cache.put(text, source, target, result)
            result
        }catch(e:Exception){
            AppLogger.w("MLKit failed, trying SimplyTranslate: $e")
            try{
                val r = SimplyTranslateService.translate(text, source, target)
                cache.put(text, source, target, r)
                r
            }catch(e2:Exception){ text }
        }
    }
}
