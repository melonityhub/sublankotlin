package com.androyal.subxplayer.ui.screens


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.androyal.subxplayer.ui.navigation.Screen

@Composable
fun NetworkScreen(navController: NavController){
    var url by remember{ mutableStateOf("")}
    var history by remember{ mutableStateOf(listOf("https://demo.unified-streaming.com/k8s/features/stream/foreman.m3u8","https://example.com/video.mp4"))}
    Scaffold(topBar={ TopAppBar(title={Text("Network Stream")}, navigationIcon={ IconButton(onClick={navController.popBackStack()}){ Icon(Icons.Default.ArrowBack,null)}})}){
        padding ->
        Column(Modifier.padding(padding).padding(16.dp).fillMaxSize(), verticalArrangement=Arrangement.spacedBy(16.dp)){
            OutlinedTextField(value=url, onValueChange={url=it}, label={Text("Stream URL (HLS/DASH/RTSP)")}, modifier=Modifier.fillMaxWidth(), singleLine=true)
            Button(onClick={
                if(url.isNotBlank()){
                    history = listOf(url)+history
                    navController.navigate(Screen.Player.create(android.net.Uri.encode(url), android.net.Uri.encode("Network Stream")))
                }
            }, modifier=Modifier.fillMaxWidth()){ Text("Play") }
            Text("Recent:", style=MaterialTheme.typography.titleSmall)
            LazyColumn(verticalArrangement=Arrangement.spacedBy(8.dp)){
                items(history){ h ->
                    Card(onClick={ navController.navigate(Screen.Player.create(android.net.Uri.encode(h), android.net.Uri.encode(h))) }){ Text(h, Modifier.padding(12.dp)) }
                }
            }
        }
    }
}
