package com.androyal.subxplayer.playback

import android.content.Context
import android.net.Uri
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.MimeTypes
import androidx.media3.common.Player
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.MediaSource
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.media3.exoplayer.hls.HlsMediaSource
import androidx.media3.exoplayer.dash.DashMediaSource
import androidx.media3.exoplayer.rtsp.RtspMediaSource
import com.androyal.subxplayer.data.models.SubtitleCue
import com.androyal.subxplayer.data.models.VideoItem
import com.androyal.subxplayer.utils.AppLogger
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

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _isBuffering = MutableStateFlow(false)
    val isBuffering: StateFlow<Boolean> = _isBuffering

    fun getPlayer(): ExoPlayer {
        if (player == null) {
            val factory = DefaultDataSource.Factory(context)
            player = ExoPlayer.Builder(context)
                .setMediaSourceFactory(
                    androidx.media3.exoplayer.source.DefaultMediaSourceFactory(context)
                )
                .build().apply {
                    addListener(object : Player.Listener {
                        override fun onIsPlayingChanged(isPlaying: Boolean) { _isPlaying.value = isPlaying }
                        override fun onPlaybackStateChanged(state: Int) {
                            when (state) {
                                Player.STATE_BUFFERING -> { _isBuffering.value = true; AppLogger.d("Player buffering") }
                                Player.STATE_READY -> {
                                    _isBuffering.value = false
                                    _durationMs.value = duration.coerceAtLeast(0)
                                    _error.value = null
                                    AppLogger.d("Player ready duration=$duration")
                                }
                                Player.STATE_ENDED -> { _isPlaying.value = false; AppLogger.d("Player ended") }
                                Player.STATE_IDLE -> { _isBuffering.value = false }
                            }
                        }
                        override fun onPlayerError(err: androidx.media3.common.PlaybackException) {
                            _isBuffering.value = false
                            _error.value = err.message ?: "Playback error ${err.errorCodeName}"
                            AppLogger.w("Player error: $err code=${err.errorCode}")
                        }
                    })
                    // Enable handling of all formats: keep default
                    playWhenReady = true
                }
        }
        return player!!
    }

    private fun buildMediaItem(uri: Uri, mimeHint: String? = null): MediaItem {
        val uriStr = uri.toString().lowercase()
        val mime = when {
            mimeHint != null -> mimeHint
            uriStr.endsWith(".m3u8") || uriStr.contains("m3u8") -> MimeTypes.APPLICATION_M3U8
            uriStr.endsWith(".mpd") -> MimeTypes.APPLICATION_MPD
            uriStr.endsWith(".rtsp") -> MimeTypes.APPLICATION_RTSP
            uriStr.endsWith(".mp3") -> MimeTypes.AUDIO_MPEG
            uriStr.endsWith(".wav") -> MimeTypes.AUDIO_WAV
            uriStr.endsWith(".flac") -> MimeTypes.AUDIO_FLAC
            uriStr.endsWith(".mkv") -> MimeTypes.VIDEO_MATROSKA
            uriStr.endsWith(".mov") -> MimeTypes.VIDEO_MP4
            uriStr.endsWith(".avi") -> "video/x-msvideo"
            uriStr.endsWith(".webm") -> MimeTypes.VIDEO_WEBM
            uriStr.endsWith(".mp4") -> MimeTypes.VIDEO_MP4
            else -> null
        }
        return MediaItem.Builder()
            .setUri(uri)
            .apply { if (mime != null) setMimeType(mime) }
            .build()
    }

    fun play(video: VideoItem, startPositionMs: Long = 0L) {
        _currentVideo.value = video
        _error.value = null
        try {
            val p = getPlayer()
            p.stop()
            p.clearMediaItems()
            val uri = Uri.parse(video.uri)
            val item = buildMediaItem(uri, video.mimeType)
            p.setMediaItem(item)
            p.prepare()
            if (startPositionMs > 0) p.seekTo(startPositionMs)
            p.playWhenReady = true
            AppLogger.d("Play video: ${video.displayName} uri=${video.uri} pos=$startPositionMs")
        } catch (e: Exception) {
            _error.value = e.message ?: "Failed to play"
            AppLogger.w("play failed", e)
        }
    }

    fun playUri(uri: Uri, name: String = "", mimeType: String? = null) {
        val display = name.ifBlank { uri.lastPathSegment ?: "video" }
        play(VideoItem(id = uri.toString(), uri = uri.toString(), displayName = display, mimeType = mimeType))
    }

    fun playWithSubtitles(uri: Uri, name: String, subtitleUris: List<Uri> = emptyList()) {
        _currentVideo.value = VideoItem(id = uri.toString(), uri = uri.toString(), displayName = name)
        _error.value = null
        try {
            val p = getPlayer()
            p.stop()
            p.clearMediaItems()
            val builder = MediaItem.Builder().setUri(uri)
            if (subtitleUris.isNotEmpty()) {
                val configs = subtitleUris.map { subUri ->
                    val mime = when {
                        subUri.toString().endsWith(".vtt", true) -> MimeTypes.TEXT_VTT
                        subUri.toString().endsWith(".srt", true) -> MimeTypes.APPLICATION_SUBRIP
                        else -> MimeTypes.APPLICATION_SUBRIP
                    }
                    MediaItem.SubtitleConfiguration.Builder(subUri)
                        .setMimeType(mime)
                        .setLanguage("en")
                        .setSelectionFlags(C.SELECTION_FLAG_DEFAULT)
                        .build()
                }
                builder.setSubtitleConfigurations(configs)
            }
            p.setMediaItem(builder.build())
            p.prepare()
            p.playWhenReady = true
        } catch (e: Exception) {
            _error.value = e.message ?: "Failed to play with subs"
            AppLogger.w("playWithSubtitles failed", e)
        }
    }

    fun addExternalSubtitle(uri: Uri, mime: String = MimeTypes.APPLICATION_SUBRIP) {
        // For ExoPlayer, external subs are most reliably handled via our overlay (SubtitlesManager)
        // so we store cues parsed from the file; but also try to inject into player if possible
        try {
            val text = context.contentResolver.openInputStream(uri)?.bufferedReader()?.readText() ?: return
            val cues = com.androyal.subxplayer.utils.SubtitleParsingService.parse(text, mime)
            if (cues.isNotEmpty()) loadSubtitles(cues)
        } catch (e: Exception) {
            AppLogger.w("addExternalSubtitle failed", e)
        }
    }

    fun seekTo(ms: Long) {
        try { getPlayer().seekTo(ms.coerceAtLeast(0)) } catch (_: Exception) {}
    }
    fun seekBy(deltaMs: Long) {
        val p = getPlayer()
        seekTo((p.currentPosition + deltaMs).coerceIn(0, p.duration.coerceAtLeast(0)))
    }
    fun setSpeed(speed: Float) {
        try { getPlayer().setPlaybackSpeed(speed.coerceIn(0.25f, 4f)) } catch (_: Exception) {}
    }
    fun pause() { try { getPlayer().pause() } catch (_: Exception) {} }
    fun resume() { try { getPlayer().play() } catch (_: Exception) {} }
    fun togglePlayPause() {
        val p = getPlayer()
        if (p.isPlaying) p.pause() else p.play()
    }
    fun release() { try { player?.release() } catch (_: Exception) {}; player = null }

    fun loadSubtitles(cues: List<SubtitleCue>) { _cues.value = cues }

    fun updatePosition(pos: Long) {
        _positionMs.value = pos
        val active = _cues.value.firstOrNull { pos in it.startMs until it.endMs }
        if (active != _activeCue.value) _activeCue.value = active
    }

    fun clearError() { _error.value = null }
}
