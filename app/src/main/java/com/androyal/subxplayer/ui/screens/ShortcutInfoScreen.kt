package com.androyal.subxplayer.ui.screens


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun ShortcutInfoScreen(navController: NavController){
    val shortcuts = listOf("Space" to "Play/Pause","← →" to "Seek 10s","↑ ↓" to "Volume","F" to "Fullscreen","M" to "Mute","C" to "Captions","N/P" to "Next/Prev subtitle", "A" to "AB Repeat", "S" to "Screenshot")
    Scaffold(topBar={ TopAppBar(title={Text("Shortcuts")}, navigationIcon={ IconButton(onClick={navController.popBackStack()}){ Text("←")}})}){
        padding ->
        LazyColumn(Modifier.padding(padding).padding(16.dp), verticalArrangement=Arrangement.spacedBy(8.dp)){
            items(shortcuts.size){ i ->
                val (k,v) = shortcuts[i]
                Card(Modifier.fillMaxWidth()){ Row(Modifier.padding(12.dp).fillMaxWidth(), horizontalArrangement=Arrangement.SpaceBetween){ Text(k, style=MaterialTheme.typography.titleMedium); Text(v) } }
            }
        }
    }
}
