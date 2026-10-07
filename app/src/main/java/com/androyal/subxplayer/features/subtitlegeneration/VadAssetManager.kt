
package com.androyal.subxplayer.features.subtitlegeneration

import android.content.Context
import java.io.File

object VadAssetManager {
    fun getVadModelPath(context: Context): String {
        val out = File(context.filesDir, "models/silero_vad.onnx")
        if(!out.exists()){
            out.parentFile?.mkdirs()
            context.assets.open("models/silero_vad.onnx").use { ins ->
                out.outputStream().use { ins.copyTo(it) }
            }
        }
        return out.absolutePath
    }
}
