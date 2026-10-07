package com.androyal.subxplayer.playback

import javax.inject.Inject
import javax.inject.Singleton

// Port of playback_manager_deps.dart
@Singleton
class PlaybackManagerDeps @Inject constructor() {
    fun handle() {}
}

data class PlaybackManagerDepsState(val dummy:String="")
