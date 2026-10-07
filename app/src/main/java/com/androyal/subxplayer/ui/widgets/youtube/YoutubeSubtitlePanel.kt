
package com.androyal.subxplayer.ui.widgets.youtube

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.androyal.subxplayer.data.models.SubtitleCue

@Composable
fun YoutubeSubtitlePanel(cues:List<SubtitleCue>, activeCue:SubtitleCue?, onCueClick:(SubtitleCue)->Unit, modifier:Modifier=Modifier){
    LazyColumn(modifier, verticalArrangement=Arrangement.spacedBy(8.dp), contentPadding=PaddingValues(12.dp)){
        items(cues){ cue ->
            Card(
                onClick={onCueClick(cue)},
                colors=CardDefaults.cardColors(containerColor= if(cue==activeCue) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant),
                modifier=Modifier.fillMaxWidth()
            ){
                Column(Modifier.padding(12.dp)){
                    Text(cue.text, style=MaterialTheme.typography.bodyMedium)
                    cue.translatedText?.let{ Text(it, style=MaterialTheme.typography.bodySmall, color=MaterialTheme.colorScheme.primary)}
                }
            }
        }
    }
}
