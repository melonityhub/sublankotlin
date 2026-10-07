package com.androyal.subxplayer.playback

import javax.inject.Inject
import javax.inject.Singleton

// Port of playback_screen_signals.dart
@Singleton
class PlaybackScreenSignals @Inject constructor() {
    fun handle() {}
}

data class PlaybackScreenSignalsState(val dummy:String="")
