package com.androyal.subxplayer.ui.widgets.sheets

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.androyal.subxplayer.data.models.SubtitleSettings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerMoreSheet(
    onDismiss: () -> Unit,
    onExportSrt: () -> Unit,
    onExportVtt: () -> Unit,
    onExportTxt: () -> Unit,
    onStyling: () -> Unit,
    subtitleSettings: SubtitleSettings,
    onSettingsChange: (SubtitleSettings) -> Unit
) {
    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("More Options", style = MaterialTheme.typography.titleLarge)

            // Subtitle styling
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Subtitle Style", style = MaterialTheme.typography.titleSmall)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedButton(onClick = { onSettingsChange(subtitleSettings.copy(fontSizeSp = (subtitleSettings.fontSizeSp - 2).coerceAtLeast(12f))) }) { Text("A-") }
                        Text("${subtitleSettings.fontSizeSp.toInt()}sp", modifier = Modifier.padding(horizontal = 8.dp))
                        OutlinedButton(onClick = { onSettingsChange(subtitleSettings.copy(fontSizeSp = (subtitleSettings.fontSizeSp + 2).coerceAtMost(32f))) }) { Text("A+") }
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        FilterChip(selected = subtitleSettings.secondaryEnabled, onClick = { onSettingsChange(subtitleSettings.copy(secondaryEnabled = !subtitleSettings.secondaryEnabled)) }, label = { Text("Dual Subs") })
                        FilterChip(selected = subtitleSettings.selectableText, onClick = { onSettingsChange(subtitleSettings.copy(selectableText = !subtitleSettings.selectableText)) }, label = { Text("Selectable") })
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        ChoiceChip("Bottom", subtitleSettings.position.name == "BOTTOM") { onSettingsChange(subtitleSettings.copy(position = com.androyal.subxplayer.data.models.SubtitlePosition.BOTTOM)) }
                        ChoiceChip("Top", subtitleSettings.position.name == "TOP") { onSettingsChange(subtitleSettings.copy(position = com.androyal.subxplayer.data.models.SubtitlePosition.TOP)) }
                        ChoiceChip("Center", subtitleSettings.position.name == "CENTER") { onSettingsChange(subtitleSettings.copy(position = com.androyal.subxplayer.data.models.SubtitlePosition.CENTER)) }
                    }
                }
            }

            // Export
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Export Subtitles", style = MaterialTheme.typography.titleSmall)
                    Text("Save as SRT, VTT, TXT or PDF. Share or use for Anki mining.", style = MaterialTheme.typography.bodySmall)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                        Button(onClick = onExportSrt, modifier = Modifier.weight(1f)) { Text("SRT") }
                        OutlinedButton(onClick = onExportVtt, modifier = Modifier.weight(1f)) { Text("VTT") }
                        OutlinedButton(onClick = onExportTxt, modifier = Modifier.weight(1f)) { Text("TXT") }
                    }
                }
            }

            // Other
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    ListTile(Icons.Default.Cast, "Cast with subtitles", "Bilingual casting & controls") {}
                    ListTile(Icons.Default.ContentCut, "SubX Clip", "Create clip from subtitle moment") {}
                    ListTile(Icons.Default.Share, "Share Video", "Share with subtitles") {}
                }
            }

            Button(onClick = onDismiss, modifier = Modifier.fillMaxWidth()) { Text("Close") }
            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
private fun ChoiceChip(label: String, selected: Boolean, onClick: () -> Unit) {
    FilterChip(selected = selected, onClick = onClick, label = { Text(label) })
}

@Composable
private fun ListTile(icon: androidx.compose.ui.graphics.vector.ImageVector, title: String, subtitle: String, onClick: () -> Unit) {
    Row(Modifier.fillMaxWidth().padding(vertical = 6.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        Icon(icon, null, modifier = Modifier.size(20.dp))
        Column(Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.bodyMedium)
            Text(subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
