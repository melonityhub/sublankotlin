package com.androyal.subxplayer.ui.screens


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import android.webkit.WebView
import android.webkit.WebViewClient

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YoutubeVideoScreen(navController: NavController, videoId:String){
    Scaffold(topBar={ TopAppBar(title={Text("YouTube Player")}, navigationIcon={ IconButton(onClick={navController.popBackStack()}){ Text("←")}})}){
        padding ->
        Column(Modifier.padding(padding).fillMaxSize()){
            AndroidView(factory={ ctx ->
                WebView(ctx).apply{
                    webViewClient=WebViewClient()
                    settings.javaScriptEnabled=true
                    loadUrl("https://www.youtube.com/embed/$videoId")
                }
            }, modifier=Modifier.fillMaxWidth().height(220.dp))
            // Subtitle panel below
            Text("Subtitles for $videoId", Modifier.padding(16.dp))
        }
    }
}
