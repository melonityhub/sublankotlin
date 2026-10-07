package com.androyal.subxplayer.ui.screens


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun PlayerControlsSettingsScreen(navController: NavController){
    Scaffold(topBar={ TopAppBar(title={Text("Player Controls")}, navigationIcon={ IconButton(onClick={navController.popBackStack()}){ Text("←")}})}){
        padding ->
        LazyColumn(Modifier.padding(padding).padding(16.dp), verticalArrangement=Arrangement.spacedBy(12.dp)){
            item{ Text("Customize which buttons appear on the player bar")}
            item{
                listOf("Play/Pause","Seek bar","Speed","Subtitle","Audio","Cast","Pip","Fullscreen","Next/Prev").forEach{ name ->
                    var checked by remember{ mutableStateOf(true)}
                    Card(Modifier.fillMaxWidth()){ Row(Modifier.padding(12.dp).fillMaxWidth(), horizontalArrangement=Arrangement.SpaceBetween){ Text(name); Switch(checked=checked, onCheckedChange={checked=it}) } }
                }
            }
        }
    }
}
