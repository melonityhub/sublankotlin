
package com.androyal.subxplayer.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.androyal.subxplayer.ui.navigation.Screen
import com.androyal.subxplayer.ui.widgets.settings.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavController, vm: SettingsViewModel = hiltViewModel()){
    val state by vm.state.collectAsState()
    Scaffold(topBar ={ TopAppBar(title={Text("Settings")}, navigationIcon ={IconButton(onClick={navController.popBackStack()}){ Icon(Icons.Default.ArrowBack,null)}}) }){
        padding ->
        LazyColumn(Modifier.padding(padding).fillMaxSize(), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)){
            item{
                SettingsSection(title="Playback"){
                    SettingsRow(title="Hardware decoding", subtitle="Use MediaCodec when available", trailing={ Switch(checked=state.hwDecoding, onCheckedChange={ vm.setHwDecoding(it)})})
                    SettingsRow(title="Seek duration", value="${state.seekDuration}s", onClick={})
                    SettingsRow(title="Default speed", value="${state.defaultSpeed}x", onClick={})
                    SettingsRow(title="Resume", subtitle="Continue from last position", trailing={ Switch(checked=state.resumeEnabled, onCheckedChange={})})
                }
            }
            item{
                SettingsSection(title="Subtitles"){
                    SettingsRow(title="Primary language", value=state.primaryLang, onClick={ navController.navigate(Screen.ManageOfflineLang.route)})
                    SettingsRow(title="Secondary language", value=state.secondaryLang?:"None", onClick={})
                    SettingsRow(title="Appearance", subtitle="Font, colors, position", onClick={})
                    SettingsRow(title="Generate subtitles", subtitle="Whisper / Sherpa-ONNX", onClick={ navController.navigate(Screen.ManageAsr.route)})
                }
            }
            item{
                SettingsSection(title="Translation"){
                    SettingsRow(title="Target language", value=state.translationTarget, onClick={})
                    SettingsRow(title="Offline languages", subtitle="Manage MLKit packs", onClick={ navController.navigate(Screen.ManageOfflineLang.route)})
                    SettingsRow(title="Translation mirrors", subtitle=state.catalogBaseUrl, onClick={})
                }
            }
            item{
                SettingsSection(title="Appearance"){
                    SettingsRow(title="Theme", value=state.themeMode.name, onClick={})
                    SettingsRow(title="Player controls", subtitle="Customize buttons", onClick={ navController.navigate(Screen.PlayerControls.route)})
                    SettingsRow(title="Gestures", subtitle="Swipe, pinch, double-tap", onClick={ navController.navigate(Screen.PlayerAdvanced.route)})
                }
            }
            item{
                SettingsSection(title="Advanced"){
                    SettingsRow(title="Casting", subtitle="Google Cast", onClick={})
                    SettingsRow(title="Shortcuts", subtitle="Keyboard & remote", onClick={ navController.navigate(Screen.Shortcuts.route)})
                    SettingsRow(title="Storage", subtitle="Cache & exports", onClick={})
                    SettingsRow(title="Secrets & Endpoints", subtitle="Configure API keys & URLs", onClick={ vm.showSecretsDialog() })
                }
            }
            item{
                SettingsSection(title="About"){
                    SettingsRow(title="Version", value="2.3.1 (47)")
                    SettingsRow(title="Premium", value=if(state.isPremium) "SubX+ Active" else "Upgrade to SubX+", onClick={ navController.navigate(Screen.Premium.route)})
                    SettingsRow(title="Privacy policy", onClick={})
                    SettingsRow(title="Rate & feedback", onClick={})
                }
            }
        }
    }
    if(state.showSecrets){
        SecretsConfigDialog(
            catalogUrl = state.catalogBaseUrl,
            revenueCatKey = state.revenueCatKey,
            subsUrl = state.subsBaseUrl,
            firebaseKey = state.firebaseKey,
            onDismiss = { vm.hideSecrets() },
            onSave = { c,r,s,f -> vm.saveSecrets(c,r,s,f) }
        )
    }
}
