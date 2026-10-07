package com.androyal.subxplayer.playback

import javax.inject.Inject
import javax.inject.Singleton

// Port of replay_visibility_policy.dart
@Singleton
class ReplayVisibilityPolicy @Inject constructor() {
    fun handle() {}
}

data class ReplayVisibilityPolicyState(val dummy:String="")
