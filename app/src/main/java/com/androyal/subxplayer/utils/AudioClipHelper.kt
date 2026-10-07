package com.androyal.subxplayer.utils

import android.content.Context
import java.io.File

object AudioClipHelper {
    suspend fun clipAudio(context: Context, sourcePath:String, startMs:Long, endMs:Long, outFile: File): File? {
        return try {
            // FFmpegKit: ffmpeg -i source -ss start -to end -c copy out
            outFile
        } catch(e:Exception){ AppLogger.e("clipAudio failed", e); null }
    }
}
