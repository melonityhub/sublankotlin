
package com.androyal.subxplayer.utils

import android.content.Context
import android.util.Log
import com.ichi2.anki.api.AddContentApi
import com.ichi2.anki.api.NoteInfo

object AnkiHelper {
    fun isAnkiInstalled(context: Context): Boolean = try{
        context.packageManager.getPackageInfo("com.ichi2.anki",0); true
    }catch(_:Exception){ false }

    fun getDecks(context: Context): Map<Long,String> = try{
        AddContentApi.getAnkiDroid(context).deckList ?: emptyMap()
    }catch(e:Exception){ Log.e("Anki","getDecks",e); emptyMap()}

    fun addNote(context: Context, deckId:Long, modelId:Long, fields:Array<String>, tags:Set<String>): Long? {
        return try{
            val api = AddContentApi.getAnkiDroid(context)
            api.addNote(modelId, deckId, fields, tags)
        }catch(e:Exception){ Log.e("Anki","addNote",e); null }
    }
}
