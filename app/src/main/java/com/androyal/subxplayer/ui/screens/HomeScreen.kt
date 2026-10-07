package com.androyal.subxplayer.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.androyal.subxplayer.ui.navigation.Screen
import com.androyal.subxplayer.ui.widgets.browser.BrowserTopAppBar
import com.androyal.subxplayer.ui.widgets.browser.VideoListContent
import com.androyal.subxplayer.ui.widgets.browser.FolderListContent
import com.androyal.subxplayer.utils.PermissionHelper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, viewModel: HomeViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    // Permission launcher - request only when needed
    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { results ->
        val granted = results.values.all { it } || results.values.any { it }
        // For Android 14 partial access, also consider VISUAL_USER_SELECTED
        val hasAny = results.entries.any { it.value }
        viewModel.onPermissionResult(granted || hasAny)
    }

    // File picker - supports all formats: MKV, MP4, AVI, MOV, M3U/M3U8, MP3, WAV, FLAC etc
    val pickerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        if (uri != null) {
            viewModel.onPickedUri(uri)
            // Immediately open player on pick
            try {
                val name = uri.lastPathSegment ?: "video"
                navController.navigate(Screen.Player.create(Uri.encode(uri.toString()), Uri.encode(name)))
            } catch (_: Exception) {}
        }
    }

    // Check permission on first composition - don't auto-request, show UI first
    LaunchedEffect(Unit) {
        // Refresh already done in VM init
    }

    Scaffold(
        topBar = {
            BrowserTopAppBar(
                title = "SubX Player",
                onSearch = { viewModel.onSearch(it) },
                onSettings = { navController.navigate(Screen.Settings.route) },
                onCast = { },
                isCasting = false
            )
        },
        bottomBar = {
            NavigationBar(containerColor = MaterialTheme.colorScheme.surface, tonalElevation = 8.dp) {
                NavigationBarItem(selected = true, onClick = {}, icon = { Icon(Icons.Default.VideoLibrary, null) }, label = { Text("Library") })
                NavigationBarItem(selected = false, onClick = { navController.navigate(Screen.Network.route) }, icon = { Icon(Icons.Default.Language, null) }, label = { Text("Network") })
                NavigationBarItem(selected = false, onClick = { navController.navigate(Screen.YoutubeList.route) }, icon = { Icon(Icons.Default.PlayCircle, null) }, label = { Text("YouTube") })
                NavigationBarItem(selected = false, onClick = { navController.navigate(Screen.Settings.route) }, icon = { Icon(Icons.Default.Settings, null) }, label = { Text("Settings") })
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    // Open system picker for any video/audio
                    pickerLauncher.launch(arrayOf("video/*", "audio/*", "application/*"))
                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Pick video")
            }
        }
    ) { padding ->
        Box(
            Modifier
                .padding(padding)
                .fillMaxSize()
                .then(
                    Modifier // subtle gradient background
                )
        ) {
            when {
                !uiState.hasPermission -> {
                    // Permission placeholder - only request when user taps
                    PermissionPlaceholder(
                        onRequest = { permissionLauncher.launch(PermissionHelper.mediaPermissionsToRequest()) },
                        onPickAnyway = { pickerLauncher.launch(arrayOf("video/*", "audio/*", "application/*")) }
                    )
                }
                uiState.isLoading -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            CircularProgressIndicator()
                            Text("Scanning media library...", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
                uiState.videos.isEmpty() && uiState.folders.isEmpty() -> {
                    EmptyLibraryPlaceholder(
                        onPick = { pickerLauncher.launch(arrayOf("video/*", "audio/*", "application/*")) },
                        onRefresh = { viewModel.refresh() },
                        onRequestPerm = { permissionLauncher.launch(PermissionHelper.mediaPermissionsToRequest()) },
                        hasPermission = uiState.hasPermission
                    )
                }
                else -> {
                    Column(Modifier.fillMaxSize()) {
                        // Search + Breadcrumb + pull to refresh hint
                        if (uiState.breadcrumb.isNotEmpty()) {
                            ScrollableTabRow(selectedTabIndex = uiState.breadcrumb.lastIndex, edgePadding = 16.dp) {
                                Tab(selected = false, onClick = { viewModel.navigateToBreadcrumb(-1) }, text = { Text("Root") })
                                uiState.breadcrumb.forEachIndexed { i, name ->
                                    Tab(selected = i == uiState.breadcrumb.lastIndex, onClick = { viewModel.navigateToBreadcrumb(i) }, text = { Text(name) })
                                }
                            }
                        }
                        if (uiState.searchQuery.isNotBlank()) {
                            Surface(Modifier.fillMaxWidth(), color = MaterialTheme.colorScheme.secondaryContainer) {
                                Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Icon(Icons.Default.Search, null, modifier = Modifier.size(16.dp))
                                    Text("${uiState.videos.size} results for \"${uiState.searchQuery}\"", style = MaterialTheme.typography.bodySmall)
                                    Spacer(Modifier.weight(1f))
                                    TextButton(onClick = { viewModel.onSearch("") }) { Text("Clear") }
                                }
                            }
                        }
                        FolderListContent(
                            folders = uiState.folders,
                            onFolderClick = { viewModel.openFolder(it) },
                            onPlayFolder = { viewModel.playFolder(it) }
                        )
                        VideoListContent(
                            videos = uiState.videos,
                            onVideoClick = { video ->
                                // Navigate with encoded uri - ensure click works immediately
                                try {
                                    navController.navigate(Screen.Player.create(Uri.encode(video.uri), Uri.encode(video.displayName)))
                                } catch (e: Exception) {
                                    android.util.Log.e("HomeScreen", "nav failed", e)
                                }
                            },
                            onVideoLongPress = { }
                        )
                    }
                }
            }
            uiState.error?.let { err ->
                Snackbar(
                    modifier = Modifier.align(Alignment.BottomCenter).padding(16.dp),
                    action = { TextButton(onClick = { viewModel.clearError() }) { Text("Dismiss") } }
                ) { Text(err) }
            }
        }
    }
}

@Composable
private fun PermissionPlaceholder(onRequest: () -> Unit, onPickAnyway: () -> Unit) {
    Column(
        Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(Icons.Default.Folder, contentDescription = null, modifier = Modifier.size(72.dp), tint = MaterialTheme.colorScheme.primary)
        Spacer(Modifier.height(16.dp))
        Text("Media Permission Needed", style = MaterialTheme.typography.headlineSmall, textAlign = TextAlign.Center)
        Spacer(Modifier.height(8.dp))
        Text(
            "SubX needs access to your videos and audio to build your library. You can also pick files directly without granting full access.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(20.dp))
        Button(onClick = onRequest, modifier = Modifier.fillMaxWidth()) {
            Icon(Icons.Default.FolderShared, null, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(8.dp))
            Text("Grant Media Access")
        }
        Spacer(Modifier.height(8.dp))
        OutlinedButton(onClick = onPickAnyway, modifier = Modifier.fillMaxWidth()) {
            Icon(Icons.Default.VideoLibrary, null, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(8.dp))
            Text("Pick a Video Instead")
        }
        Spacer(Modifier.height(8.dp))
        Text("Your files stay private on-device.", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun EmptyLibraryPlaceholder(onPick: () -> Unit, onRefresh: () -> Unit, onRequestPerm: () -> Unit, hasPermission: Boolean) {
    Column(
        Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier.size(96.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Movie, contentDescription = null, modifier = Modifier.size(64.dp), tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f))
        }
        Spacer(Modifier.height(12.dp))
        Text("No videos yet", style = MaterialTheme.typography.headlineSmall, textAlign = TextAlign.Center)
        Text(
            "We didn't find any videos. Try picking a file, granting permission, or pulling to refresh.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(20.dp))
        Button(onClick = onPick, modifier = Modifier.fillMaxWidth()) {
            Icon(Icons.Default.Add, null, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(8.dp))
            Text("Pick Video / Audio")
        }
        Spacer(Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
            OutlinedButton(onClick = onRefresh, modifier = Modifier.weight(1f)) {
                Icon(Icons.Default.Refresh, null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(6.dp))
                Text("Refresh")
            }
            if (!hasPermission) {
                OutlinedButton(onClick = onRequestPerm, modifier = Modifier.weight(1f)) {
                    Icon(Icons.Default.FolderShared, null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("Permissions")
                }
            }
        }
        Spacer(Modifier.height(16.dp))
        Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant), modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("Supported formats", style = MaterialTheme.typography.titleSmall)
                Text("MKV • MP4 • AVI • MOV • WebM • M3U/M3U8 • MP3 • WAV • FLAC • AAC • OGG", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text("Subtitles: SRT • VTT • LRC  —  Dual subtitles & offline translate 130+ languages", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}
