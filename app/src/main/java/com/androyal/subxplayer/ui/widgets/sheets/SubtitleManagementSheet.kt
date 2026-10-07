package com.androyal.subxplayer.ui.widgets.sheets

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.androyal.subxplayer.data.models.SubtitleCue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubtitleManagementSheet(
    cues: List<SubtitleCue>,
    activeIndex: Int = -1,
    onDismiss: () -> Unit,
    onCueClick: (SubtitleCue) -> Unit,
    onCueEdit: ((SubtitleCue) -> Unit)? = null,
    onCueDelete: ((SubtitleCue) -> Unit)? = null,
    onAnkiExport: ((SubtitleCue) -> Unit)? = null,
    onPickFile: (() -> Unit)? = null,
    onGenerate: (() -> Unit)? = null
) {
    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Subtitles", style = MaterialTheme.typography.titleLarge, modifier = Modifier.weight(1f))
                Badge { Text("${cues.size}") }
            }
            if (cues.isEmpty()) {
                Card(Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.Subtitles, null, modifier = Modifier.size(32.dp))
                        Text("No subtitles loaded", style = MaterialTheme.typography.titleSmall)
                        Text("Import SRT/VTT/LRC or generate with AI offline.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            } else {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(selected = true, onClick = {}, label = { Text("All") })
                    FilterChip(selected = false, onClick = {}, label = { Text("Dual") })
                    FilterChip(selected = false, onClick = {}, label = { Text("Bookmarked") })
                }
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.heightIn(max = 420.dp)) {
                    itemsIndexed(cues) { idx, cue ->
                        val isActive = idx == activeIndex
                        Card(
                            onClick = { onCueClick(cue) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = if (isActive) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    if (isActive) Icon(Icons.Default.PlayArrow, null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.primary)
                                    Text(cue.text, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(1f))
                                }
                                if (cue.translatedText != null) Text(cue.translatedText, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary)
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    Text(formatTime(cue.startMs) + " → " + formatTime(cue.endMs), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    Spacer(Modifier.weight(1f))
                                    // Actions
                                    IconButton(onClick = { onCueEdit?.invoke(cue) }, modifier = Modifier.size(24.dp)) { Icon(Icons.Default.Edit, null, modifier = Modifier.size(14.dp)) }
                                    IconButton(onClick = { onAnkiExport?.invoke(cue) }, modifier = Modifier.size(24.dp)) { Icon(Icons.Default.Star, null, modifier = Modifier.size(14.dp)) }
                                    if (onCueDelete != null) IconButton(onClick = { onCueDelete(cue) }, modifier = Modifier.size(24.dp)) { Icon(Icons.Default.Delete, null, modifier = Modifier.size(14.dp)) }
                                }
                            }
                        }
                    }
                }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                OutlinedButton(onClick = { onPickFile?.invoke() }, modifier = Modifier.weight(1f)) {
                    Icon(Icons.Default.FileOpen, null, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("Import")
                }
                Button(onClick = { onGenerate?.invoke() }, modifier = Modifier.weight(1f)) {
                    Icon(Icons.Default.Star, null, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("Generate")
                }
            }
            Text("Tap a line to seek • Long press to copy/translate • Edit timing & text", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

private fun formatTime(ms: Long): String {
    val s = ms / 1000
    val m = s / 60
    val h = m / 60
    return if (h > 0) String.format("%d:%02d:%02d.%03d", h, m % 60, s % 60, ms % 1000)
    else String.format("%02d:%02d.%03d", m % 60, s % 60, ms % 1000)
}
