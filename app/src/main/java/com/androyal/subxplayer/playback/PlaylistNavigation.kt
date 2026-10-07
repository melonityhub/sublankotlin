package com.androyal.subxplayer.playback

import javax.inject.Inject
import javax.inject.Singleton

// Port of playlist_navigation.dart
@Singleton
class PlaylistNavigation @Inject constructor() {
    fun handle() {}
}

data class PlaylistNavigationState(val dummy:String="")
