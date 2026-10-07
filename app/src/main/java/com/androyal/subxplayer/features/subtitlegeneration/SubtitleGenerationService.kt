
package com.androyal.subxplayer.features.subtitlegeneration

import android.content.Context
import com.androyal.subxplayer.data.models.SubtitleCue
import com.androyal.subxplayer.utils.AppLogger
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

data class GenerationProgress(val stage:String, val percent:Int, val message:String)

@Singleton
class SubtitleGenerationService @Inject constructor(@ApplicationContext private val context: Context) {
    private val _progress = MutableStateFlow<GenerationProgress?>(null)
    val progress: StateFlow<GenerationProgress?> = _progress

    suspend fun generate(videoPath:String, modelId:String = "whisper_base", language:String="en"): List<SubtitleCue> = withContext(Dispatchers.IO){
        _progress.value = GenerationProgress("extracting", 10, "Extracting audio...")
        // 1) Extract audio via FFmpegKit
        val audioFile = extractAudio(videoPath)
        _progress.value = GenerationProgress("vad", 30, "Detecting speech...")
        // 2) VAD segmentation using silero_vad.onnx bundled
        // 3) Run ASR model via sherpa-onnx (lib)
        _progress.value = GenerationProgress("transcribing", 60, "Transcribing...")
        // Stub: in real build, call sherpa_onnx JNI
        kotlinx.coroutines.delay(1200)
        _progress.value = GenerationProgress("done", 100, "Done")
        // Return demo cues for now, real implementation would parse whisper output
        listOf(
            SubtitleCue(0, 0, 2500, "Hello, welcome to SubX Player."),
            SubtitleCue(1, 3000, 6000, "This is an auto-generated subtitle."),
            SubtitleCue(2, 6500, 9000, "You can edit or translate it.")
        ).also { AppLogger.d("Generated ${it.size} cues for $videoPath") }
    }

    private fun extractAudio(videoPath:String): File {
        val out = File(context.cacheDir, "extracted_${System.currentTimeMillis()}.wav")
        // FFmpegKit would be invoked here: ffmpeg -i videoPath -ar 16000 -ac 1 out
        return out
    }
}
