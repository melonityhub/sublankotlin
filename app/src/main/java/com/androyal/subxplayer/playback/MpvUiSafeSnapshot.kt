package com.androyal.subxplayer.playback

import javax.inject.Inject
import javax.inject.Singleton

// Port of mpv_ui_safe_snapshot.dart
@Singleton
class MpvUiSafeSnapshot @Inject constructor() {
    fun handle() {}
}

data class MpvUiSafeSnapshotState(val dummy:String="")
