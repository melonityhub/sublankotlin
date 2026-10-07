package com.androyal.subxplayer.utils

import com.androyal.subxplayer.data.models.SubtitleCue
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object SubtitleManager {
    private val _cues = MutableStateFlow<List<SubtitleCue>>(emptyList())
    val cues: StateFlow<List<SubtitleCue>> = _cues
    fun setCues(list:List<SubtitleCue>){ _cues.value=list }
    fun addCue(cue:SubtitleCue){ _cues.value=_cues.value+cue }
    fun clear(){ _cues.value=emptyList() }
}
