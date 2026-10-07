package com.androyal.subxplayer.ui.screens


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.androyal.subxplayer.ui.navigation.Screen
import androidx.hilt.navigation.compose.hiltViewModel
import com.androyal.subxplayer.data.repository.SettingsRepository

@Composable
fun OnboardingScreen(navController: NavController, repo: SettingsRepository = androidx.hilt.navigation.compose.hiltViewModel<OnboardingViewModel>().repo){
    var lang by remember{ mutableStateOf("en")}
    Column(Modifier.fillMaxSize().padding(24.dp), verticalArrangement=Arrangement.SpaceBetween){
        Column(verticalArrangement=Arrangement.spacedBy(16.dp)){
            Text("Welcome to SubX", style=MaterialTheme.typography.headlineLarge)
            Text("Learn languages by watching videos with dual subtitles.", style=MaterialTheme.typography.bodyLarge)
            Text("Choose your learning language:")
            listOf("en" to "English", "fa" to "فارسی", "es" to "Español", "ja" to "日本語", "fr" to "Français", "de" to "Deutsch").forEach{ (code,name) ->
                FilterChip(selected=lang==code, onClick={lang=code}, label={Text(name)}, modifier=Modifier.padding(4.dp))
            }
        }
        Button(onClick={
            // save
            navController.navigate(Screen.Home.route){ popUpTo(Screen.Onboarding.route){ inclusive=true } }
        }, modifier=Modifier.fillMaxWidth()){ Text("Get Started") }
    }
}
class OnboardingViewModel @javax.inject.Inject constructor(val repo: SettingsRepository): androidx.lifecycle.ViewModel()
