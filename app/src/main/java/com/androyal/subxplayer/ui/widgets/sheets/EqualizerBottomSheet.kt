
package com.androyal.subxplayer.ui.widgets.sheets

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EqualizerBottomSheet(onDismiss:()->Unit){
    ModalBottomSheet(onDismissRequest=onDismiss){
        Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement=Arrangement.spacedBy(12.dp)){
            Text("Equalizer", style=MaterialTheme.typography.titleLarge)
            Text("5-band equalizer with presets: Normal, Bass boost, Classical, etc.", style=MaterialTheme.typography.bodyMedium)
            Button(onClick=onDismiss, modifier=Modifier.fillMaxWidth()){ Text("Close") }
        }
    }
}
