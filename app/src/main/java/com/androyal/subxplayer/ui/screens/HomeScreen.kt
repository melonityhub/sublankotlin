
package com.androyal.subxplayer.ui.screens

import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.androyal.subxplayer.ui.navigation.Screen
import com.androyal.subxplayer.ui.widgets.browser.BrowserTopAppBar
import com.androyal.subxplayer.ui.widgets.browser.VideoListContent
import com.androyal.subxplayer.ui.widgets.browser.FolderListContent
import com.androyal.subxplayer.ui.widgets.custom.GradientBackground

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, viewModel: HomeViewModel = hiltViewModel()){
    val uiState by viewModel.uiState.collectAsState()
    Scaffold(
        topBar = {
            BrowserTopAppBar(
                title = "SubX Player",
                onSearch = { viewModel.onSearch(it) },
                onSettings = { navController.navigate(Screen.Settings.route) },
                onCast = { /* cast sheet */ },
                isCasting = false
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(selected = true, onClick = {}, icon = { Icon(Icons.Default.VideoLibrary, null)}, label = { Text("Library")})
                NavigationBarItem(selected = false, onClick = { navController.navigate(Screen.Network.route)}, icon = { Icon(Icons.Default.Language, null)}, label = { Text("Network")})
                NavigationBarItem(selected = false, onClick = { navController.navigate(Screen.YoutubeList.route)}, icon = { Icon(Icons.Default.PlayCircle, null)}, label = { Text("YouTube")})
                NavigationBarItem(selected = false, onClick = { navController.navigate(Screen.Settings.route)}, icon = { Icon(Icons.Default.Settings, null)}, label = { Text("Settings")})
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { viewModel.pickMedia() }){
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ){ padding ->
        GradientBackground {
            Column(Modifier.padding(padding).fillMaxSize()){
                if(uiState.isLoading){
                    Box(Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center){
                        CircularProgressIndicator()
                    }
                } else {
                    // Breadcrumb
                    if(uiState.breadcrumb.isNotEmpty()){
                        ScrollableTabRow(selectedTabIndex = uiState.breadcrumb.lastIndex){
                            uiState.breadcrumb.forEachIndexed{ i, name ->
                                Tab(selected = i==uiState.breadcrumb.lastIndex, onClick = { viewModel.navigateToBreadcrumb(i)}, text={ Text(name) })
                            }
                        }
                    }
                    // Folder + Video lists
                    FolderListContent(folders = uiState.folders, onFolderClick = { viewModel.openFolder(it) }, onPlayFolder = { viewModel.playFolder(it) })
                    VideoListContent(videos = uiState.videos, onVideoClick = { video ->
                        navController.navigate(Screen.Player.create(Uri.encode(video.uri), Uri.encode(video.displayName)))
                    }, onVideoLongPress = { /* sheet */})
                }
            }
        }
    }
}
