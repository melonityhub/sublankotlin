
package com.androyal.subxplayer.ui.widgets.sheets

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenerateSubtitleDialog(onDismiss:()->Unit){
    ModalBottomSheet(onDismissRequest=onDismiss){
        Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement=Arrangement.spacedBy(12.dp)){
            Text("GenerateSubtitle", style=MaterialTheme.typography.titleLarge)
            Text("Generate subtitles: model selection (Whisper etc.), language, VAD toggle.", style=MaterialTheme.typography.bodyMedium)
            Button(onClick=onDismiss, modifier=Modifier.fillMaxWidth()){ Text("Close") }
        }
    }
}
