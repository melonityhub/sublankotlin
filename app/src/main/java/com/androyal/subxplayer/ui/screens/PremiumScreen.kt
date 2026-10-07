package com.androyal.subxplayer.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PremiumScreen(navController: NavController){
    Scaffold(topBar={
        TopAppBar(
            title={Text("SubX+")},
            navigationIcon={ IconButton(onClick={navController.popBackStack()}){ Text("←") }})
    }){
        padding ->
        Column(Modifier.padding(padding).fillMaxSize().padding(24.dp), verticalArrangement=Arrangement.spacedBy(16.dp), horizontalAlignment=Alignment.CenterHorizontally){
            Icon(Icons.Default.CheckCircle, contentDescription=null, modifier=Modifier.size(72.dp), tint=MaterialTheme.colorScheme.primary)
            Text("All Features Unlocked", style=MaterialTheme.typography.headlineMedium, color=MaterialTheme.colorScheme.primary)
            Text("This build has all premium features enabled for free. No purchase required — enjoy full SubX experience!", style=MaterialTheme.typography.bodyLarge)
            Card(Modifier.fillMaxWidth(), colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.primaryContainer)){
                Column(Modifier.padding(20.dp), verticalArrangement=Arrangement.spacedBy(8.dp)){
                    Text("✓ Premium active - Forever", style=MaterialTheme.typography.titleMedium, color=MaterialTheme.colorScheme.onPrimaryContainer)
                    Text("Entitlement: plus (unlocked)", style=MaterialTheme.typography.bodySmall, color=MaterialTheme.colorScheme.onPrimaryContainer)
                    Text("• Higher accuracy transcription", style=MaterialTheme.typography.bodySmall, color=MaterialTheme.colorScheme.onPrimaryContainer)
                    Text("• Unlimited subtitle generation & translation", style=MaterialTheme.typography.bodySmall, color=MaterialTheme.colorScheme.onPrimaryContainer)
                    Text("• Anki export & offline translation 130+ languages", style=MaterialTheme.typography.bodySmall, color=MaterialTheme.colorScheme.onPrimaryContainer)
                    Text("• No ads", style=MaterialTheme.typography.bodySmall, color=MaterialTheme.colorScheme.onPrimaryContainer)
                }
            }
            Text("Features included:", style=MaterialTheme.typography.titleMedium)
            Column(verticalArrangement=Arrangement.spacedBy(6.dp), modifier=Modifier.fillMaxWidth()){
                listOf(
                    "Higher accuracy for complex audio & accents",
                    "Subtitle navigation + export (SRT/VTT/TXT/PDF)",
                    "Anki integration with audio",
                    "Offline translation in 130+ languages",
                    "Dual subtitles & styling",
                    "YouTube Practice Mode"
                ).forEach{ Text("• $it", style=MaterialTheme.typography.bodyMedium) }
            }
            Spacer(Modifier.height(8.dp))
            Button(onClick={ navController.popBackStack() }, modifier=Modifier.fillMaxWidth()){
                Text("Back to Settings")
            }
        }
    }
}
