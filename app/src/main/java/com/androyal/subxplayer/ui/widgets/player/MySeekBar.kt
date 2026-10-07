
package com.androyal.subxplayer.ui.widgets.player

import androidx.compose.material3.Slider
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import kotlin.math.roundToLong

@Composable
fun MySeekBar(positionMs:Long, durationMs:Long, onSeek:(Long)->Unit, modifier:Modifier=Modifier){
    if(durationMs<=0) return
    var dragging by remember{ mutableStateOf(false)}
    var dragPos by remember{ mutableStateOf(0f)}
    Slider(
        value= if(dragging) dragPos else positionMs.toFloat(),
        onValueChange={ dragging=true; dragPos=it},
        onValueChangeFinished={ dragging=false; onSeek(dragPos.roundToLong())},
        valueRange=0f..durationMs.toFloat(),
        modifier=modifier
    )
}
