
package com.androyal.subxplayer.utils

import android.webkit.MimeTypeMap

object FileTypeDetector {
    fun getMimeType(path:String): String? {
        val ext = path.substringAfterLast('.', "").lowercase()
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(ext)
    }
    fun isVideo(path:String): Boolean = getMimeType(path)?.startsWith("video")==true || path.endsWith(".mp4",true) || path.endsWith(".mkv",true) || path.endsWith(".webm",true)
    fun isAudio(path:String): Boolean = getMimeType(path)?.startsWith("audio")==true || path.endsWith(".mp3",true) || path.endsWith(".m4a",true)
    fun isSubtitle(path:String): Boolean = path.endsWith(".srt",true) || path.endsWith(".vtt",true) || path.endsWith(".ass",true)
}
