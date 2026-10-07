# Bundled ASR Models

## silero_vad.onnx

Voice Activity Detection model bundled with the app for offline use.
This binary is required for subtitle generation.

**Source (pinned):**
https://github.com/k2-fsa/sherpa-onnx/releases/download/asr-models/silero_vad.onnx

**MD5:** d486e9c504c9034316d53a9f4e0eee2e
**File size:** ~629 KB (643,854 bytes)

> IMPORTANT: Use the k2-fsa/sherpa-onnx release copy, NOT the snakers4/silero-vad
> raw file. The sherpa-onnx release is validated for compatibility with the
> sherpa_onnx Dart package (SileroVadModelConfig, VoiceActivityDetector).

The VAD model is used to pre-segment audio before passing it to the Whisper
transcription model, improving accuracy and performance.
