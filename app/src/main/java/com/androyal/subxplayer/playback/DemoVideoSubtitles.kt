package com.androyal.subxplayer.playback

import javax.inject.Inject
import javax.inject.Singleton

// Port of demo_video_subtitles.dart
@Singleton
class DemoVideoSubtitles @Inject constructor() {
    fun handle() {}
}

data class DemoVideoSubtitlesState(val dummy:String="")
