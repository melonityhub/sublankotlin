package com.androyal.subxplayer.utils

import android.content.Context
import android.util.Log

object AnkiHelper {
    fun isAnkiInstalled(context: Context): Boolean = try{
        context.packageManager.getPackageInfo("com.ichi2.anki",0); true
    }catch(_:Exception){ false }

    fun getDecks(context: Context): Map<Long,String> = try{
        // Use reflection to avoid hard dependency on Anki API artifact
        val clazz = Class.forName("com.ichi2.anki.api.AddContentApi")
        val method = clazz.getMethod("getAnkiDroid", Context::class.java)
        val api = method.invoke(null, context)
        val deckListMethod = api.javaClass.getMethod("getDeckList")
        @Suppress("UNCHECKED_CAST")
        (deckListMethod.invoke(api) as? Map<Long,String>) ?: emptyMap()
    }catch(e:Exception){ Log.e("Anki","getDecks",e); emptyMap()}

    fun addNote(context: Context, deckId:Long, modelId:Long, fields:Array<String>, tags:Set<String>): Long? {
        return try{
            val clazz = Class.forName("com.ichi2.anki.api.AddContentApi")
            val method = clazz.getMethod("getAnkiDroid", Context::class.java)
            val api = method.invoke(null, context)
            val addNoteMethod = api.javaClass.getMethod("addNote", Long::class.java, Long::class.java, Array<String>::class.java, Set::class.java)
            addNoteMethod.invoke(api, modelId, deckId, fields, tags) as? Long
        }catch(e:Exception){ Log.e("Anki","addNote",e); null }
    }
}
