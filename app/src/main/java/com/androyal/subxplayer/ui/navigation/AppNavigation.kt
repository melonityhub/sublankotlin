
package com.androyal.subxplayer.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.androyal.subxplayer.ui.screens.*

sealed class Screen(val route:String){
    data object Home: Screen("home")
    data object Player: Screen("player?uri={uri}&name={name}"){ fun create(uri:String, name:String) = "player?uri=$uri&name=$name" }
    data object Settings: Screen("settings")
    data object Premium: Screen("premium")
    data object Onboarding: Screen("onboarding")
    data object Network: Screen("network")
    data object YoutubeList: Screen("youtube_list")
    data object YoutubePlayer: Screen("youtube/{videoId}"){ fun create(id:String) = "youtube/$id" }
    data object ManageAsr: Screen("manage_asr")
    data object ManageOfflineLang: Screen("manage_offline_langs")
    data object PlayerAdvanced: Screen("player_advanced")
    data object PlayerControls: Screen("player_controls")
    data object Shortcuts: Screen("shortcuts")
    data object Demo: Screen("demo")
}

@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController(),
    onPipEnter: ()->Unit = {},
    sharedVideoUri: android.net.Uri? = null
){
    NavHost(navController, startDestination = Screen.Home.route){
        composable(Screen.Home.route){ HomeScreen(navController) }
        composable(Screen.Player.route){ backStack ->
            val uri = backStack.arguments?.getString("uri") ?: ""
            val name = backStack.arguments?.getString("name") ?: ""
            PlayerScreen(navController, uri, name, onPipEnter)
        }
        composable(Screen.Settings.route){ SettingsScreen(navController) }
        composable(Screen.Premium.route){ PremiumScreen(navController) }
        composable(Screen.Onboarding.route){ OnboardingScreen(navController) }
        composable(Screen.Network.route){ NetworkScreen(navController) }
        composable(Screen.YoutubeList.route){ YoutubeListScreen(navController) }
        composable(Screen.YoutubePlayer.route){ backStack ->
            val id = backStack.arguments?.getString("videoId") ?: ""
            YoutubeVideoScreen(navController, id)
        }
        composable(Screen.ManageAsr.route){ ManageAsrModelsScreen(navController) }
        composable(Screen.ManageOfflineLang.route){ ManageOfflineLanguagesScreen(navController) }
        composable(Screen.PlayerAdvanced.route){ PlayerAdvancedSettingsScreen(navController) }
        composable(Screen.PlayerControls.route){ PlayerControlsSettingsScreen(navController) }
        composable(Screen.Shortcuts.route){ ShortcutInfoScreen(navController) }
        composable(Screen.Demo.route){ DemoScreen(navController) }
    }
}
