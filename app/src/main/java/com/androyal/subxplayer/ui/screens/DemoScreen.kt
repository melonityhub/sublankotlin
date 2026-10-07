package com.androyal.subxplayer.ui.screens

import android.net.Uri


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.androyal.subxplayer.ui.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DemoScreenScreen(navController: NavController){
    Scaffold(topBar={ TopAppBar(title={Text("Demo")}) }){
        padding ->
        Column(Modifier.padding(padding).padding(24.dp).fillMaxSize(), verticalArrangement=Arrangement.spacedBy(16.dp)){
            Text("Demo video with sample subtitles in 20+ languages.")
            Button(onClick={ navController.navigate(Screen.Player.create(android.net.Uri.encode("android.resource://com.androyal.subxplayer/demo"), netUriEncodeHack("Demo")))} ) { Text("Play demo")}
            Text("Available subtitle tracks: EN, FA, ES, FR, DE, JA, KO, AR, RU, ZH...")
        }
    }
}
fun netUriEncodeHack(s:String)=android.net.Uri.encode(s)
