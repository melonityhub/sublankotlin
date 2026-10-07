
package com.androyal.subxplayer.utils

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class SharedVideoManager(private val context: Context) {
    suspend fun resolveDisplayName(uri: Uri): String? = withContext(Dispatchers.IO){
        try {
            context.contentResolver.query(uri, arrayOf(MediaStore.MediaColumns.DISPLAY_NAME, MediaStore.Video.Media.TITLE), null, null, null)?.use { c ->
                if(c.moveToFirst()){
                    val idx = c.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME)
                    if(idx>=0) c.getString(idx)?.takeIf{it.isNotBlank()}?.let{ return@withContext it }
                    val idx2 = c.getColumnIndex(MediaStore.Video.Media.TITLE)
                    if(idx2>=0) c.getString(idx2)?.takeIf{it.isNotBlank()}?.let{ return@withContext it }
                }
            }
        } catch(e:Exception){ Log.e("SharedVideo", "resolve name failed", e) }
        null
    }
    suspend fun getFileSize(uri: Uri): Long? = withContext(Dispatchers.IO){
        try{
            if(uri.scheme=="content"){
                context.contentResolver.query(uri, arrayOf(MediaStore.MediaColumns.SIZE), null,null,null)?.use{c->
                    if(c.moveToFirst()){
                        val i=c.getColumnIndex(MediaStore.MediaColumns.SIZE)
                        if(i>=0) return@withContext c.getLong(i)
                    }
                }
                context.contentResolver.query(uri, arrayOf("_size"), null,null,null)?.use{c->
                    if(c.moveToFirst()){
                        val i=c.getColumnIndex("_size")
                        if(i>=0) return@withContext c.getLong(i)
                    }
                }
            } else if(uri.scheme=="file"){
                val f = File(uri.path ?: return@withContext null)
                if(f.exists()) return@withContext f.length()
            }
        }catch(e:Exception){ Log.e("FileSize","err",e)}
        null
    }
}
