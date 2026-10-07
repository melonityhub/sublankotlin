package com.androyal.subxplayer.ui.screens


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.androyal.subxplayer.features.subtitlegeneration.AsrModelCatalog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageAsrModelsScreenScreen(navController: NavController){
    Scaffold(topBar={ TopAppBar(title={Text("ASR Models")}, navigationIcon={ IconButton(onClick={navController.popBackStack()}){ Text("←")}})}){
        padding ->
        LazyColumn(Modifier.padding(padding).fillMaxSize(), contentPadding=PaddingValues(16.dp), verticalArrangement=Arrangement.spacedBy(12.dp)){
            items(AsrModelCatalog.models){ m ->
                Card(Modifier.fillMaxWidth()){
                    Column(Modifier.padding(16.dp), verticalArrangement=Arrangement.spacedBy(8.dp)){
                        Text(m.displayName, style=MaterialTheme.typography.titleMedium)
                        Text("${m.language} • ${m.sizeMb} MB • ${m.type}", style=MaterialTheme.typography.bodySmall)
                        Row(horizontalArrangement=Arrangement.spacedBy(8.dp)){
                            Button(onClick={}){ Text(if(m.isDownloaded) "Delete" else "Download")}
                            OutlinedButton(onClick={}){ Text("Info")}
                        }
                    }
                }
            }
        }
    }
}
