package com.androyal.subxplayer.playback

import javax.inject.Inject
import javax.inject.Singleton

// Port of folder_playlist_models.dart
@Singleton
class FolderPlaylistModels @Inject constructor() {
    fun handle() {}
}

data class FolderPlaylistModelsState(val dummy:String="")
