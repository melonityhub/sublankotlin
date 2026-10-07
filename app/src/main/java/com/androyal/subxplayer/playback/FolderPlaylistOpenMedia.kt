package com.androyal.subxplayer.playback

import javax.inject.Inject
import javax.inject.Singleton

// Port of folder_playlist_open_media.dart
@Singleton
class FolderPlaylistOpenMedia @Inject constructor() {
    fun handle() {}
}

data class FolderPlaylistOpenMediaState(val dummy:String="")
