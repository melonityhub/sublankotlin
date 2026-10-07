package com.androyal.subxplayer.playback

import javax.inject.Inject
import javax.inject.Singleton

// Port of playback_resume_lookup.dart
@Singleton
class PlaybackResumeLookup @Inject constructor() {
    fun handle() {}
}

data class PlaybackResumeLookupState(val dummy:String="")
