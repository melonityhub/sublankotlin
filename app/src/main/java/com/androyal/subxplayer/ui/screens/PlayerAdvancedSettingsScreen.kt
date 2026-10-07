package com.androyal.subxplayer.ui.screens


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun PlayerAdvancedSettingsScreen(navController: NavController){
    Scaffold(topBar={ TopAppBar(title={Text("Advanced Player")}, navigationIcon={ IconButton(onClick={navController.popBackStack()}){ Text("←")}})}){
        padding ->
        LazyColumn(Modifier.padding(padding).padding(16.dp), verticalArrangement=Arrangement.spacedBy(16.dp)){
            item{ Text("Hardware decoding, auto-pause, auto-repeat, bilingual audio, etc.", style=MaterialTheme.typography.bodyMedium)}
            item{
                Card(Modifier.fillMaxWidth()){ Column(Modifier.padding(16.dp), verticalArrangement=Arrangement.spacedBy(12.dp)){
                    Text("Auto-pause duration"); Slider(value=0.5f, onValueChange={})
                    Text("Auto-repeat count"); Slider(value=1f, onValueChange={}, valueRange=0f..5f)
                    Text("Bilingual audio delay"); Slider(value=0f, onValueChange={})
                }}
            }
        }
    }
}
