package com.androyal.subxplayer.ui.widgets.sheets

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.androyal.subxplayer.data.models.SubtitleCue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubtitleEditDialog(cue: SubtitleCue, onDismiss: () -> Unit, onSave: (String, Long, Long) -> Unit) {
    var text by remember { mutableStateOf(cue.text) }
    var start by remember { mutableStateOf(cue.startMs.toString()) }
    var end by remember { mutableStateOf(cue.endMs.toString()) }
    var showDeleteConfirm by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Subtitle") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(value = text, onValueChange = { text = it }, label = { Text("Text") }, modifier = Modifier.fillMaxWidth(), minLines = 2)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = start, onValueChange = { start = it }, label = { Text("Start ms") }, modifier = Modifier.weight(1f))
                    OutlinedTextField(value = end, onValueChange = { end = it }, label = { Text("End ms") }, modifier = Modifier.weight(1f))
                }
                Text("Tip: You can adjust timing to fix sync issues.", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        },
        confirmButton = {
            Button(onClick = {
                val s = start.toLongOrNull() ?: cue.startMs
                val e = end.toLongOrNull() ?: cue.endMs
                onSave(text, s, e)
            }) { Text("Save") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}
