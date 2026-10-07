
package com.androyal.subxplayer.ui.widgets.player

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ResumeBanner(positionMs:Long, onResume:()->Unit, onRestart:()->Unit, modifier:Modifier=Modifier){
    Card(modifier, colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.surfaceVariant)){
        Row(Modifier.padding(12.dp), horizontalArrangement=Arrangement.spacedBy(12.dp)){
            Column(Modifier.weight(1f)){
                Text("Continue watching?", style=MaterialTheme.typography.titleSmall)
                Text("Resume from ${positionMs/1000}s", style=MaterialTheme.typography.bodySmall)
            }
            TextButton(onClick=onRestart){ Text("Restart")}
            Button(onClick=onResume){ Text("Resume")}
        }
    }
}
