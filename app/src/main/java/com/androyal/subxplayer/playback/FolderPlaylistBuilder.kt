package com.androyal.subxplayer.playback

import javax.inject.Inject
import javax.inject.Singleton

// Port of folder_playlist_builder.dart
@Singleton
class FolderPlaylistBuilder @Inject constructor() {
    fun handle() {}
}

data class FolderPlaylistBuilderState(val dummy:String="")
