package com.androyal.subxplayer.ui.screens


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.hilt.navigation.compose.hiltViewModel
import com.androyal.subxplayer.utils.RevenueCatService

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PremiumScreenScreen(navController: NavController){
    val isPremium by RevenueCatService.isPremium.collectAsState()
    var restoreMsg by remember{ mutableStateOf<String?>(null)}
    Scaffold(topBar={ TopAppBar(title={Text("SubX+")}, navigationIcon={ IconButton(onClick={navController.popBackStack()}){ Text("←") }}) }){
        padding ->
        Column(Modifier.padding(padding).fillMaxSize().padding(24.dp), verticalArrangement=Arrangement.spacedBy(16.dp), horizontalAlignment=Alignment.CenterHorizontally){
            Text("Unlock SubX+", style=MaterialTheme.typography.headlineMedium)
            Text(if(isPremium) "All premium features unlocked. Thank you!" else "Get higher accuracy transcription, unlimited translations, and ad-free experience.", style=MaterialTheme.typography.bodyLarge)
            if(!isPremium){
                Button(onClick={ /* purchase */ }, modifier=Modifier.fillMaxWidth()){ Text("Upgrade - Monthly") }
                OutlinedButton(onClick={ /* yearly */ }, modifier=Modifier.fillMaxWidth()){ Text("Yearly - Save 40%") }
                OutlinedButton(onClick={ RevenueCatService.restore{ ok -> restoreMsg = if(ok) "Premium restored!" else "No purchase found" }}, modifier=Modifier.fillMaxWidth()){ Text("Restore purchases") }
                restoreMsg?.let{ Text(it, color=MaterialTheme.colorScheme.primary)}
            } else {
                Card(Modifier.fillMaxWidth()){ Column(Modifier.padding(16.dp)){ Text("✓ Premium active"); Text("Entitlement: plus") } }
            }
            Text("Features:", style=MaterialTheme.typography.titleMedium)
            Column(verticalArrangement=Arrangement.spacedBy(8.dp)){
                listOf("Higher accuracy for complex audio & accents","Subtitle navigation + export","Anki integration","Offline translation in 130+ languages","No ads, forever").forEach{ Text("• $it") }
            }
        }
    }
}
