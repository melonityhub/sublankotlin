
package com.androyal.subxplayer.playback

import android.content.Context
import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.androyal.subxplayer.data.models.SubtitleCue
import com.androyal.subxplayer.data.models.VideoItem
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlaybackManager @Inject constructor(@ApplicationContext private val context: Context) {
    private var player: ExoPlayer? = null

    private val _currentVideo = MutableStateFlow<VideoItem?>(null)
    val currentVideo: StateFlow<VideoItem?> = _currentVideo

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying

    private val _positionMs = MutableStateFlow(0L)
    val positionMs: StateFlow<Long> = _positionMs

    private val _durationMs = MutableStateFlow(0L)
    val durationMs: StateFlow<Long> = _durationMs

    private val _cues = MutableStateFlow<List<SubtitleCue>>(emptyList())
    val cues: StateFlow<List<SubtitleCue>> = _cues

    private val _activeCue = MutableStateFlow<SubtitleCue?>(null)
    val activeCue: StateFlow<SubtitleCue?> = _activeCue

    fun getPlayer(): ExoPlayer {
        if(player==null){
            player = ExoPlayer.Builder(context).build().apply{
                addListener(object: Player.Listener{
                    override fun onIsPlayingChanged(isPlaying:Boolean){ _isPlaying.value=isPlaying }
                    override fun onPlaybackStateChanged(state:Int){
                        if(state==Player.STATE_READY) _durationMs.value = duration
                    }
                })
            }
        }
        return player!!
    }

    fun play(video: VideoItem, startPositionMs: Long = 0L){
        _currentVideo.value = video
        val p = getPlayer()
        val item = MediaItem.fromUri(Uri.parse(video.uri))
        p.setMediaItem(item)
        p.prepare()
        if(startPositionMs>0) p.seekTo(startPositionMs)
        p.playWhenReady = true
    }

    fun playUri(uri: Uri, name:String = ""){
        play(VideoItem(id=uri.toString(), uri=uri.toString(), displayName=name.ifBlank{ uri.lastPathSegment?:"video" }))
    }

    fun seekTo(ms:Long){ getPlayer().seekTo(ms) }
    fun setSpeed(speed:Float){ getPlayer().setPlaybackSpeed(speed) }
    fun pause(){ getPlayer().pause() }
    fun resume(){ getPlayer().play() }
    fun release(){ player?.release(); player=null }

    fun loadSubtitles(cues:List<SubtitleCue>){ _cues.value=cues }

    fun updatePosition(pos:Long){
        _positionMs.value = pos
        _activeCue.value = _cues.value.firstOrNull{ pos in it.startMs until it.endMs }
    }
}
