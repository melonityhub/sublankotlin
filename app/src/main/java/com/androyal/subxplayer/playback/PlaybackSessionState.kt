package com.androyal.subxplayer.playback

import javax.inject.Inject
import javax.inject.Singleton

// Port of playback_session_state.dart
@Singleton
class PlaybackSessionState @Inject constructor() {
    fun handle() {}
}

data class PlaybackSessionStateState(val dummy:String="")
