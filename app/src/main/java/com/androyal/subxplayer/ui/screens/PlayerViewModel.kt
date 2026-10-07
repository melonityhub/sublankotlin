
package com.androyal.subxplayer.ui.screens

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androyal.subxplayer.data.models.SubtitleCue
import com.androyal.subxplayer.data.models.SubtitleSettings
import com.androyal.subxplayer.data.repository.PlaybackStateRepository
import com.androyal.subxplayer.playback.PlaybackManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PlayerUiState(
    val title:String="",
    val isPlaying:Boolean=false,
    val positionMs:Long=0L,
    val durationMs:Long=0L,
    val isLoading:Boolean=true,
    val loadingMessage:String="Loading...",
    val cues:List<SubtitleCue> = emptyList(),
    val activeCue:SubtitleCue? = null,
    val subtitleSettings:SubtitleSettings = SubtitleSettings(),
    val speed:Float=1f,
    val showSubtitleSheet:Boolean=false,
    val showSpeedSheet:Boolean=false,
    val showResumeBanner:Boolean=false,
    val resumePosition:Long=0L
)

@HiltViewModel
class PlayerViewModel @Inject constructor(
    val playbackManager: PlaybackManager,
    private val playbackRepo: PlaybackStateRepository
): ViewModel(){
    private val _uiState = MutableStateFlow(PlayerUiState())
    val uiState: StateFlow<PlayerUiState> = _uiState
    private var ticker: Job? = null

    init {
        viewModelScope.launch{
            playbackManager.isPlaying.collect{ _uiState.value = _uiState.value.copy(isPlaying=it) }
        }
        viewModelScope.launch{
            playbackManager.activeCue.collect{ _uiState.value = _uiState.value.copy(activeCue=it) }
        }
        startTicker()
    }

    private fun startTicker(){
        ticker?.cancel()
        ticker = viewModelScope.launch{
            while(isActive){
                val p = playbackManager.getPlayer()
                _uiState.value = _uiState.value.copy(positionMs=p.currentPosition, durationMs=p.duration.coerceAtLeast(0))
                playbackManager.updatePosition(p.currentPosition)
                delay(200)
            }
        }
    }

    fun openUri(uri:Uri, name:String){
        viewModelScope.launch{
            _uiState.value = _uiState.value.copy(title=name, isLoading=true)
            val lastPos = playbackRepo.getLastPosition(uri.toString())
            if(lastPos>5000) _uiState.value = _uiState.value.copy(showResumeBanner=true, resumePosition=lastPos)
            playbackManager.playUri(uri, name)
            _uiState.value = _uiState.value.copy(isLoading=false)
        }
    }

    fun togglePlayPause(){ val p=playbackManager.getPlayer(); if(p.isPlaying) p.pause() else p.play() }
    fun seekTo(ms:Long){ playbackManager.seekTo(ms) }
    fun setSpeed(s:Float){ playbackManager.setSpeed(s); _uiState.value=_uiState.value.copy(speed=s) }
    fun next(){}
    fun prev(){}
    fun showSubtitleSheet(){ _uiState.value=_uiState.value.copy(showSubtitleSheet=true) }
    fun showSpeedSheet(){ _uiState.value=_uiState.value.copy(showSpeedSheet=true) }
    fun showMoreSheet(){}
    fun hideSheets(){ _uiState.value=_uiState.value.copy(showSubtitleSheet=false, showSpeedSheet=false) }
    fun resume(){ val pos=_uiState.value.resumePosition; seekTo(pos); _uiState.value=_uiState.value.copy(showResumeBanner=false) }
    fun restart(){ seekTo(0); _uiState.value=_uiState.value.copy(showResumeBanner=false) }

    override fun onCleared(){ ticker?.cancel(); super.onCleared() }
}
