
package com.androyal.subxplayer.ui.widgets.sheets

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.androyal.subxplayer.data.models.SubtitleCue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubtitleManagementSheet(cues:List<SubtitleCue>, onDismiss:()->Unit, onCueClick:(SubtitleCue)->Unit){
    ModalBottomSheet(onDismissRequest=onDismiss){
        Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement=Arrangement.spacedBy(12.dp)){
            Text("Subtitles", style=MaterialTheme.typography.titleLarge)
            Row(horizontalArrangement=Arrangement.spacedBy(8.dp)){
                FilterChip(selected=true, onClick={}, label={Text("All")})
                FilterChip(selected=false, onClick={}, label={Text("Bookmarked")})
                FilterChip(selected=false, onClick={}, label={Text("Translated")})
            }
            LazyColumn(verticalArrangement=Arrangement.spacedBy(8.dp), modifier=Modifier.heightIn(max=400.dp)){
                items(cues){ cue ->
                    Card(onClick={onCueClick(cue)}, modifier=Modifier.fillMaxWidth()){
                        Column(Modifier.padding(12.dp)){
                            Text(cue.text, style=MaterialTheme.typography.bodyMedium)
                            if(cue.translatedText!=null) Text(cue.translatedText, style=MaterialTheme.typography.bodySmall, color=MaterialTheme.colorScheme.primary)
                            Text("${cue.startMs/1000}s - ${cue.endMs/1000}s", style=MaterialTheme.typography.labelSmall)
                        }
                    }
                }
            }
            Row(horizontalArrangement=Arrangement.spacedBy(8.dp), modifier=Modifier.fillMaxWidth()){
                OutlinedButton(onClick={}, modifier=Modifier.weight(1f)){ Text("Import")}
                Button(onClick={}, modifier=Modifier.weight(1f)){ Text("Generate")}
            }
        }
    }
}
