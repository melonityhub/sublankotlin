
package com.androyal.subxplayer.ui.widgets.sheets

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpeedControlBottomSheet(currentSpeed:Float, onSpeedChange:(Float)->Unit, onDismiss:()->Unit){
    ModalBottomSheet(onDismissRequest=onDismiss){
        Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement=Arrangement.spacedBy(12.dp)){
            Text("Playback speed", style=MaterialTheme.typography.titleLarge)
            Text("Playback speed from 0.25x to 3.0x with presets. Includes pitch correction toggle.", style=MaterialTheme.typography.bodyMedium)
            Slider(value=currentSpeed, onValueChange=onSpeedChange, valueRange=0.25f..3f)
            Text("${currentSpeed}x", style=MaterialTheme.typography.titleMedium)
            Button(onClick=onDismiss, modifier=Modifier.fillMaxWidth()){ Text("Close") }
        }
    }
}
