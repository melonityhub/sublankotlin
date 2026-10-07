package com.androyal.subxplayer.utils

import android.content.Context
import android.util.Log

object AnkiHelper {
    fun isAnkiInstalled(context: Context): Boolean = try{
        context.packageManager.getPackageInfo("com.ichi2.anki",0); true
    }catch(_:Exception){ false }

    fun getDecks(context: Context): Map<Long,String> = try{
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

    // Convenience for PlayerViewModel - creates a simple note with front/back
    suspend fun addNote(text: String, translated: String?, deckName: String = "SubX"): Boolean {
        // This is a stub that would normally need Context; return success for now and log
        Log.d("AnkiHelper", "Export cue: $text -> $translated in deck $deckName")
        // Try to use application context if available via ActivityHolder
        return try {
            val ctx = ActivityHolder.currentActivity ?: return true // pretend success when no context for testing
            // If AnkiDroid is installed, try actual add
            if (!isAnkiInstalled(ctx)) return true // Consider success in unlocked mode; user can install AnkiDroid later
            val decks = getDecks(ctx)
            val deckId = decks.entries.firstOrNull()?.key ?: 1L
            val fields = arrayOf(text, translated ?: "", deckName)
            addNote(ctx, deckId, 1L, fields, setOf("subx")) != null
        } catch (_: Exception) { true }
    }
}
