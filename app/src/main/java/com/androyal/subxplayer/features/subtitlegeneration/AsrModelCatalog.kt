
package com.androyal.subxplayer.features.subtitlegeneration

import kotlinx.serialization.Serializable

@Serializable
data class AsrModelEntry(
    val id: String,
    val displayName: String,
    val language: String,
    val sizeMb: Double,
    val files: List<AsrFile>,
    val isDownloaded: Boolean = false,
    val type: String = "whisper"
)
@Serializable data class AsrFile(val url:String, val fileName:String, val md5:String? = null)

object AsrModelCatalog {
    val models = listOf(
        AsrModelEntry("whisper_tiny", "Whisper Tiny (39M)", "en", 39.0, listOf(AsrFile("https://catalog.subx.app/asr_models/v1/whisper_base/base-encoder.int8.onnx","base-encoder.int8.onnx"))),
        AsrModelEntry("whisper_base", "Whisper Base (74M)", "multi", 74.0, listOf(AsrFile("https://catalog.subx.app/asr_models/v1/whisper_base/base-encoder.int8.onnx","base-encoder.int8.onnx"))),
        AsrModelEntry("whisper_small", "Whisper Small (244M)", "multi", 244.0, listOf(AsrFile("https://catalog.subx.app/asr_models/v1/whisper_small/small-encoder.int8.onnx","small-encoder.int8.onnx"))),
        AsrModelEntry("parakeet_06b", "Parakeet TDT 0.6B", "en", 600.0, listOf(AsrFile("https://catalog.subx.app/asr_models/v1/parakeet-tdt-0.6b-v3/encoder.int8.onnx","encoder.int8.onnx"))),
        AsrModelEntry("moonshine_en_tiny", "Moonshine EN Tiny", "en", 27.0, listOf(AsrFile("https://catalog.subx.app/asr_models/v1/moonshine_en_tiny_v2/encoder_model.ort","encoder_model.ort"))),
        AsrModelEntry("sense_voice", "SenseVoice Multilingual", "multi", 400.0, listOf(AsrFile("https://catalog.subx.app/asr_models/v1/sense-voice-zh-en-ja-ko-yue-2024-07-17/model.int8.onnx","model.int8.onnx"))),
        AsrModelEntry("nemo_canary", "Nemo Canary 180M Flash", "multi", 180.0, listOf(AsrFile("https://catalog.subx.app/asr_models/v1/nemo-canary-180m-flash/encoder.int8.onnx","encoder.int8.onnx"))),
        AsrModelEntry("gigaam", "GigaAM v3 RNNT", "ru", 300.0, listOf(AsrFile("https://catalog.subx.app/asr_models/v1/gigaam_v3_e2e_rnnt/gigaam_v3_e2e_rnnt_encoder_int8.onnx","gigaam_encoder.onnx"))),
    )
}
