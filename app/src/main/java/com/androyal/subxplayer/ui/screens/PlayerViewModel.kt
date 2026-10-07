package com.androyal.subxplayer.ui.screens

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androyal.subxplayer.data.models.SubtitleCue
import com.androyal.subxplayer.data.models.SubtitleSettings
import com.androyal.subxplayer.data.models.VideoItem
import com.androyal.subxplayer.data.repository.PlaybackStateRepository
import com.androyal.subxplayer.playback.PlaybackManager
import com.androyal.subxplayer.utils.AnkiHelper
import com.androyal.subxplayer.utils.AppLogger
import com.androyal.subxplayer.utils.SubtitleParsingService
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PlayerUiState(
    val title: String = "",
    val uri: String = "",
    val isPlaying: Boolean = false,
    val positionMs: Long = 0L,
    val durationMs: Long = 0L,
    val isLoading: Boolean = true,
    val loadingMessage: String = "Loading...",
    val cues: List<SubtitleCue> = emptyList(),
    val activeCue: SubtitleCue? = null,
    val activeCueIndex: Int = -1,
    val subtitleSettings: SubtitleSettings = SubtitleSettings(secondaryEnabled = true),
    val speed: Float = 1f,
    val showSubtitleSheet: Boolean = false,
    val showSpeedSheet: Boolean = false,
    val showMoreSheet: Boolean = false,
    val showEditDialog: Boolean = false,
    val editingCue: SubtitleCue? = null,
    val showResumeBanner: Boolean = false,
    val resumePosition: Long = 0L,
    val error: String? = null,
    val isBuffering: Boolean = false,
    // Features
    val autoPause: Boolean = false,
    val autoSkip: Boolean = false,
    val autoRepeat: Boolean = false,
    val autoRepeatCount: Int = 2,
    val autoRepeatDelayMs: Long = 800,
    val repeatRemaining: Int = 0,
    val isRepeating: Boolean = false,
    val subtitleOffsetMs: Long = 0L,
    val brightness: Float = 0.5f,
    val volume: Float = 0.8f,
    val showControls: Boolean = true,
    val subtitleListVisible: Boolean = true,
    val currentVolume: Float = 1f
)

@HiltViewModel
class PlayerViewModel @Inject constructor(
    val playbackManager: PlaybackManager,
    private val playbackRepo: PlaybackStateRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {
    private val _uiState = MutableStateFlow(PlayerUiState())
    val uiState: StateFlow<PlayerUiState> = _uiState
    private var ticker: Job? = null
    private var autoPauseJob: Job? = null
    private var controlsHideJob: Job? = null

    init {
        viewModelScope.launch { playbackManager.isPlaying.collect { _uiState.value = _uiState.value.copy(isPlaying = it) } }
        viewModelScope.launch { playbackManager.activeCue.collect { cue ->
            val idx = _uiState.value.cues.indexOfFirst { it.id == cue?.id }
            _uiState.value = _uiState.value.copy(activeCue = cue, activeCueIndex = idx)
            // Handle auto-pause
            if (cue != null && _uiState.value.autoPause) {
                scheduleAutoPause(cue)
            }
            // Handle auto-repeat
            if (cue != null && _uiState.value.autoRepeat && _uiState.value.repeatRemaining > 0) {
                // Will be handled by ticker loop
            }
        } }
        viewModelScope.launch { playbackManager.error.collect { _uiState.value = _uiState.value.copy(error = it) } }
        viewModelScope.launch { playbackManager.isBuffering.collect { _uiState.value = _uiState.value.copy(isBuffering = it) } }
        startTicker()
    }

    private fun startTicker() {
        ticker?.cancel()
        ticker = viewModelScope.launch {
            while (isActive) {
                try {
                    val p = playbackManager.getPlayer()
                    val pos = p.currentPosition.coerceAtLeast(0)
                    val dur = p.duration.coerceAtLeast(0)
                    if (dur > 0) {
                        _uiState.value = _uiState.value.copy(positionMs = pos, durationMs = dur)
                        playbackManager.updatePosition(pos + _uiState.value.subtitleOffsetMs)
                        // Auto-skip silent gaps
                        if (_uiState.value.autoSkip) handleAutoSkip(pos)
                        // Auto-repeat logic
                        if (_uiState.value.autoRepeat) handleAutoRepeat(pos)
                        // Save playback state periodically
                        if (pos % 5000 < 250) {
                            launch { playbackRepo.save(_uiState.value.uri, pos, dur, _uiState.value.speed) }
                        }
                    }
                } catch (_: Exception) {}
                delay(200)
            }
        }
    }

    private fun scheduleAutoPause(cue: SubtitleCue) {
        autoPauseJob?.cancel()
        autoPauseJob = viewModelScope.launch {
            val delayMs = (cue.endMs - (playbackManager.getPlayer().currentPosition + _uiState.value.subtitleOffsetMs)).coerceAtLeast(0)
            delay(delayMs)
            // Only pause if still on same cue
            if (_uiState.value.activeCue?.id == cue.id) {
                playbackManager.pause()
                AppLogger.d("AutoPause at cue ${cue.id}")
            }
        }
    }

    private fun handleAutoSkip(pos: Long) {
        val cues = _uiState.value.cues
        if (cues.isEmpty()) return
        val nextCue = cues.firstOrNull { it.startMs > pos + _uiState.value.subtitleOffsetMs } ?: return
        val gap = nextCue.startMs - (pos + _uiState.value.subtitleOffsetMs)
        // If gap > 2 seconds of silence, skip to next cue
        if (gap > 2000 && _uiState.value.activeCue == null) {
            // Small threshold to avoid rapid skipping
            if (gap > 3000) {
                playbackManager.seekTo(nextCue.startMs - _uiState.value.subtitleOffsetMs)
                AppLogger.d("AutoSkip gap $gap ms -> seek to ${nextCue.startMs}")
            }
        }
    }

    private var repeatCueId: Long? = null
    private var repeatCountDown: Int = 0

    private fun handleAutoRepeat(pos: Long) {
        val cue = _uiState.value.activeCue ?: return
        // If cue just started and we haven't set repeat for this cue, init
        if (repeatCueId != cue.id) {
            repeatCueId = cue.id
            repeatCountDown = _uiState.value.autoRepeatCount
            _uiState.value = _uiState.value.copy(repeatRemaining = repeatCountDown, isRepeating = repeatCountDown > 0)
        }
        // When cue ends, repeat if needed
        if (pos + _uiState.value.subtitleOffsetMs >= cue.endMs - 100 && repeatCountDown > 0) {
            repeatCountDown--
            _uiState.value = _uiState.value.copy(repeatRemaining = repeatCountDown)
            viewModelScope.launch {
                delay(_uiState.value.autoRepeatDelayMs)
                if (_uiState.value.activeCue?.id == cue.id || repeatCountDown >= 0) {
                    playbackManager.seekTo(cue.startMs - _uiState.value.subtitleOffsetMs)
                    if (repeatCountDown == 0) {
                        repeatCueId = null
                        _uiState.value = _uiState.value.copy(isRepeating = false)
                    }
                }
            }
        }
    }

    fun openUri(uri: Uri, name: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(title = name, uri = uri.toString(), isLoading = true, loadingMessage = "Loading $name...", error = null)
            try {
                val lastPos = try { playbackRepo.getLastPosition(uri.toString()) } catch (_: Exception) { 0L }
                if (lastPos > 5000) _uiState.value = _uiState.value.copy(showResumeBanner = true, resumePosition = lastPos)
                playbackManager.playUri(uri, name)
                // Try to auto-load subtitles with same name (srt/vtt alongside video) - via content resolver we can't easily
                // but we can try to look for cached subs
                loadCachedSubtitles(uri.toString())
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message ?: "Failed to open video")
                AppLogger.w("openUri failed", e)
            }
            _uiState.value = _uiState.value.copy(isLoading = false)
            // Auto hide controls after 3s
            scheduleHideControls()
        }
    }

    private suspend fun loadCachedSubtitles(videoUri: String) {
        // Stub: in real app, look up DB for subtitle bookmarks / external files
        // For demo, inject demo subs if video is demo.mp4
        if (videoUri.contains("demo")) {
            try {
                val demoCues = context.assets.open("videos/demo.en.srt").use { SubtitleParsingService.parseSrt(it) }
                if (demoCues.isNotEmpty()) {
                    _uiState.value = _uiState.value.copy(cues = demoCues)
                    playbackManager.loadSubtitles(demoCues)
                }
            } catch (_: Exception) {}
        }
    }

    fun loadSubtitlesFromUri(uri: Uri) {
        viewModelScope.launch {
            try {
                val mime = context.contentResolver.getType(uri) ?: uri.toString().substringAfterLast(".", "")
                val text = context.contentResolver.openInputStream(uri)?.bufferedReader()?.readText() ?: throw Exception("Cannot read subtitle")
                val cues = SubtitleParsingService.parse(text, mime)
                if (cues.isEmpty()) throw Exception("No cues found - check SRT/VTT/LRC format")
                _uiState.value = _uiState.value.copy(cues = cues)
                playbackManager.loadSubtitles(cues)
                AppLogger.d("Loaded ${cues.size} cues from $uri")
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = "Subtitle load failed: ${e.message}")
            }
        }
    }

    fun generateSubtitles(modelId: String = "whisper_base") {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, loadingMessage = "Generating subtitles with $modelId...")
            try {
                // Use service (stub for now returns demo)
                val svc = com.androyal.subxplayer.features.subtitlegeneration.SubtitleGenerationService(context)
                val cues = svc.generate(_uiState.value.uri, modelId, "en")
                _uiState.value = _uiState.value.copy(cues = cues)
                playbackManager.loadSubtitles(cues)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = "Generation failed: ${e.message}")
            }
            _uiState.value = _uiState.value.copy(isLoading = false)
        }
    }

    fun togglePlayPause() {
        playbackManager.togglePlayPause()
        scheduleHideControls()
    }

    fun seekTo(ms: Long) { playbackManager.seekTo(ms); scheduleHideControls() }
    fun seekBy(deltaMs: Long) { playbackManager.seekBy(deltaMs); scheduleHideControls() }
    fun seekToCue(cue: SubtitleCue) { seekTo(cue.startMs - _uiState.value.subtitleOffsetMs) }
    fun nextCue() {
        val idx = _uiState.value.activeCueIndex
        val cues = _uiState.value.cues
        if (idx >= 0 && idx + 1 < cues.size) seekToCue(cues[idx + 1])
        else if (cues.isNotEmpty()) seekToCue(cues.first())
    }
    fun prevCue() {
        val idx = _uiState.value.activeCueIndex
        val cues = _uiState.value.cues
        if (idx > 0) seekToCue(cues[idx - 1])
        else if (cues.isNotEmpty()) seekTo(maxOf(0, playbackManager.getPlayer().currentPosition - 3000))
    }

    fun setSpeed(s: Float) { playbackManager.setSpeed(s); _uiState.value = _uiState.value.copy(speed = s) }

    fun toggleAutoPause() { _uiState.value = _uiState.value.copy(autoPause = !_uiState.value.autoPause) }
    fun toggleAutoSkip() { _uiState.value = _uiState.value.copy(autoSkip = !_uiState.value.autoSkip) }
    fun toggleAutoRepeat() {
        val newVal = !_uiState.value.autoRepeat
        _uiState.value = _uiState.value.copy(autoRepeat = newVal, repeatRemaining = if (newVal) _uiState.value.autoRepeatCount else 0)
        if (!newVal) { repeatCueId = null; repeatCountDown = 0 }
    }

    fun updateSubtitleSettings(s: com.androyal.subxplayer.data.models.SubtitleSettings) {
        _uiState.value = _uiState.value.copy(subtitleSettings = s)
    }

    fun adjustSubtitleOffset(deltaMs: Long) {
        _uiState.value = _uiState.value.copy(subtitleOffsetMs = _uiState.value.subtitleOffsetMs + deltaMs)
    }

    fun editCue(cue: SubtitleCue) { _uiState.value = _uiState.value.copy(editingCue = cue, showEditDialog = true) }
    fun saveEditedCue(newText: String, newStart: Long, newEnd: Long) {
        val editing = _uiState.value.editingCue ?: return
        val updated = editing.copy(text = newText, startMs = newStart, endMs = newEnd)
        val newList = _uiState.value.cues.map { if (it.id == editing.id) updated else it }.sortedBy { it.startMs }
        _uiState.value = _uiState.value.copy(cues = newList, showEditDialog = false, editingCue = null)
        playbackManager.loadSubtitles(newList)
    }

    fun deleteCue(cue: SubtitleCue) {
        val newList = _uiState.value.cues.filter { it.id != cue.id }
        _uiState.value = _uiState.value.copy(cues = newList)
        playbackManager.loadSubtitles(newList)
    }

    fun exportSubtitles(format: String, onResult: (String) -> Unit) {
        val cues = _uiState.value.cues
        if (cues.isEmpty()) { onResult("No subtitles to export"); return }
        val text = when (format.lowercase()) {
            "srt" -> SubtitleParsingService.toSrt(cues)
            "vtt" -> SubtitleParsingService.toVtt(cues)
            "txt" -> SubtitleParsingService.toTxt(cues)
            else -> SubtitleParsingService.toSrt(cues)
        }
        // Write to cache and share via intent
        try {
            val file = java.io.File(context.cacheDir, "export_${System.currentTimeMillis()}.$format")
            file.writeText(text)
            onResult(file.absolutePath)
        } catch (e: Exception) { onResult("Export failed: ${e.message}") }
    }

    fun exportToAnki(cue: SubtitleCue) {
        viewModelScope.launch {
            try {
                AnkiHelper.addNote(cue.text, cue.translatedText, _uiState.value.title)
                _uiState.value = _uiState.value.copy(error = "Exported to Anki: ${cue.text.take(30)}")
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = "Anki export failed: ${e.message}. Install AnkiDroid.")
            }
        }
    }

    fun showSubtitleSheet() { _uiState.value = _uiState.value.copy(showSubtitleSheet = true) }
    fun showSpeedSheet() { _uiState.value = _uiState.value.copy(showSpeedSheet = true) }
    fun showMoreSheet() { _uiState.value = _uiState.value.copy(showMoreSheet = true) }
    fun hideSheets() { _uiState.value = _uiState.value.copy(showSubtitleSheet = false, showSpeedSheet = false, showMoreSheet = false, showEditDialog = false) }
    fun resume() { val pos = _uiState.value.resumePosition; seekTo(pos); _uiState.value = _uiState.value.copy(showResumeBanner = false) }
    fun restart() { seekTo(0); _uiState.value = _uiState.value.copy(showResumeBanner = false) }
    fun clearError() { _uiState.value = _uiState.value.copy(error = null); playbackManager.clearError() }
    fun toggleControls() {
        val show = !_uiState.value.showControls
        _uiState.value = _uiState.value.copy(showControls = show)
        if (show) scheduleHideControls()
    }
    private fun scheduleHideControls() {
        controlsHideJob?.cancel()
        controlsHideJob = viewModelScope.launch {
            delay(4000)
            if (_uiState.value.isPlaying) _uiState.value = _uiState.value.copy(showControls = false)
        }
    }
    fun setShowControls(v: Boolean) { _uiState.value = _uiState.value.copy(showControls = v); if (v) scheduleHideControls() }

    override fun onCleared() { ticker?.cancel(); autoPauseJob?.cancel(); controlsHideJob?.cancel(); super.onCleared() }
}
