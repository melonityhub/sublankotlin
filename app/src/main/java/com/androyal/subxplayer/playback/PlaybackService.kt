
package com.androyal.subxplayer.playback

import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PlaybackService : MediaSessionService() {
    @Inject lateinit var playbackManager: PlaybackManager
    private var mediaSession: MediaSession? = null
    override fun onCreate(){
        super.onCreate()
        val player = playbackManager.getPlayer()
        mediaSession = MediaSession.Builder(this, player).build()
    }
    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? = mediaSession
    override fun onDestroy(){
        mediaSession?.release()
        super.onDestroy()
    }
}
