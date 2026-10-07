package com.androyal.subxplayer.playback

import javax.inject.Inject
import javax.inject.Singleton

// Port of playback_session_resolver.dart
@Singleton
class PlaybackSessionResolver @Inject constructor() {
    fun handle() {}
}

data class PlaybackSessionResolverState(val dummy:String="")
