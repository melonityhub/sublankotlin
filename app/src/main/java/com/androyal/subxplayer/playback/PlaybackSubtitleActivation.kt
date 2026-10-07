package com.androyal.subxplayer.playback

import javax.inject.Inject
import javax.inject.Singleton

// Port of playback_subtitle_activation.dart
@Singleton
class PlaybackSubtitleActivation @Inject constructor() {
    fun handle() {}
}

data class PlaybackSubtitleActivationState(val dummy:String="")
