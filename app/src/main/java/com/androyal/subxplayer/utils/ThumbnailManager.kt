
package com.androyal.subxplayer.utils

import android.content.Context
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object ThumbnailManager {
    suspend fun getThumbnail(context: Context, uri: Uri, timeMs: Long = 1000): Bitmap? = withContext(Dispatchers.IO){
        try{
            val mmr = MediaMetadataRetriever()
            try{
                mmr.setDataSource(context, uri)
                mmr.getFrameAtTime(timeMs*1000, MediaMetadataRetriever.OPTION_CLOSEST_SYNC)
            } finally { try{ mmr.release()}catch(_:Exception){} }
        }catch(_:Exception){ null }
    }
}
