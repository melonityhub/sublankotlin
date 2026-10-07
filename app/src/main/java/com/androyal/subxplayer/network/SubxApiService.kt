
package com.androyal.subxplayer.network

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

data class SubsJsonResponse(val cues: List<SubsCueDto>)
data class SubsCueDto(val start: Double, val end: Double, val text: String, val translation: String? = null)
data class TranslationMirrorsResponse(val mirrors: List<String>)
data class CatalogReleaseDto(val version: Int, val url: String, val notes: String? = null)

interface SubxApiService {
    @GET("/subs/{id}/subs.json")
    suspend fun getSubs(@Path("id") youtubeId: String): SubsJsonResponse

    @GET("/catalog/release")
    suspend fun getCatalogRelease(): CatalogReleaseDto

    @GET("/config/translation_mirrors.json")
    suspend fun getTranslationMirrors(): TranslationMirrorsResponse

    // ASR models are static files on catalog.subx.app/asr_models/...
    // Downloaded via OkHttp directly, not Retrofit
}

interface TranslationApiService {
    @GET("/api/translate")
    suspend fun translate(
        @Query("text") text: String,
        @Query("source") source: String,
        @Query("target") target: String
    ): Map<String, String>
}
