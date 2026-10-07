package com.androyal.subxplayer.playback

import javax.inject.Inject
import javax.inject.Singleton

// Port of playback_open_request.dart
@Singleton
class PlaybackOpenRequest @Inject constructor() {
    fun handle() {}
}

data class PlaybackOpenRequestState(val dummy:String="")
