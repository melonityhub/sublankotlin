
package com.androyal.subxplayer.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

object SimplyTranslateService {
    // Mirrors from catalog.subx.app/config/translation_mirrors.json
    private val mirrors = listOf("https://simplytranslate.org", "https://translate.joshuarainbow.co.uk")
    suspend fun translate(text:String, source:String, target:String): String = withContext(Dispatchers.IO){
        val encoded = URLEncoder.encode(text, "UTF-8")
        for(base in mirrors){
            try{
                val url = URL("$base/api/translate/?engine=google&from=$source&to=$target&text=$encoded")
                val conn = url.openConnection() as HttpURLConnection
                conn.connectTimeout=8000; conn.readTimeout=8000
                conn.requestMethod="GET"
                if(conn.responseCode==200){
                    val body = conn.inputStream.bufferedReader().readText()
                    val json = JSONObject(body)
                    return@withContext json.optString("translated_text", text)
                }
            }catch(_:Exception){}
        }
        text
    }
}
