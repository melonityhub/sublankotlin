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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageOfflineLanguagesScreenScreen(navController: NavController){
    val langs = listOf("en" to "English","fa" to "Persian","es" to "Spanish","fr" to "French","de" to "German","ja" to "Japanese","ko" to "Korean","ar" to "Arabic","ru" to "Russian","zh" to "Chinese")
    Scaffold(topBar={ TopAppBar(title={Text("Offline Languages")}, navigationIcon={ IconButton(onClick={navController.popBackStack()}){ Text("←")}})}){
        padding ->
        LazyColumn(Modifier.padding(padding).fillMaxSize(), contentPadding=PaddingValues(16.dp), verticalArrangement=Arrangement.spacedBy(8.dp)){
            items(langs){ (code,name) ->
                Card(Modifier.fillMaxWidth()){
                    Row(Modifier.padding(16.dp).fillMaxWidth(), horizontalArrangement=Arrangement.SpaceBetween){
                        Column{ Text(name); Text(code, style=MaterialTheme.typography.bodySmall)}
                        Button(onClick={}){ Text("Download")}
                    }
                }
            }
        }
    }
}
