
package com.androyal.subxplayer.network

import android.content.Context
import com.androyal.subxplayer.data.models.YoutubeCatalogVideo
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStreamReader
import javax.inject.Inject
import javax.inject.Singleton

data class CatalogFile(
    val version:Int,
    @com.google.gson.annotations.SerializedName("schema_version") val schema_version:Int,
    val status:Int,
    @com.google.gson.annotations.SerializedName("updated_at") val updated_at:String,
    val videos:List<YoutubeCatalogVideo>
)

@Singleton
class CatalogRepository @Inject constructor(@ApplicationContext private val context: Context, private val api: SubxApiService) {
    private val gson = Gson()

    suspend fun loadBundledCatalog(): CatalogFile = withContext(Dispatchers.IO) {
        context.assets.open("yt_catalog/catalog.en.json").use { ins ->
            gson.fromJson(InputStreamReader(ins), CatalogFile::class.java)
        }
    }

    suspend fun fetchRemoteSubs(youtubeId: String): List<com.androyal.subxplayer.data.models.SubtitleCue> {
        return try {
            val res = api.getSubs(youtubeId)
            res.cues.mapIndexed { idx, dto ->
                com.androyal.subxplayer.data.models.SubtitleCue(
                    id = idx.toLong(),
                    startMs = (dto.start*1000).toLong(),
                    endMs = (dto.end*1000).toLong(),
                    text = dto.text,
                    translatedText = dto.translation
                )
            }
        } catch (e: Exception) { emptyList() }
    }
}
