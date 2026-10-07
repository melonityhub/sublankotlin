
package com.androyal.subxplayer.ui.screens

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.ui.PlayerView
import androidx.navigation.NavController
import com.androyal.subxplayer.playback.PlaybackManager
import com.androyal.subxplayer.ui.widgets.player.*
import com.androyal.subxplayer.ui.widgets.sheets.*

@Composable
fun PlayerScreen(navController: NavController, uriEncoded:String, nameEncoded:String, onPipEnter:()->Unit){
    val context = LocalContext.current
    val viewModel: PlayerViewModel = hiltViewModel()
    val uri = Uri.decode(uriEncoded)
    val name = Uri.decode(nameEncoded)
    LaunchedEffect(uri){ viewModel.openUri(Uri.parse(uri), name) }
    val uiState by viewModel.uiState.collectAsState()
    val player = viewModel.playbackManager.getPlayer()

    Box(Modifier.fillMaxSize().background(Color.Black)){
        AndroidView(factory = { ctx ->
            PlayerView(ctx).apply{
                this.player = player
                useController = false
            }
        }, modifier = Modifier.fillMaxSize())

        // Subtitle display overlay
        SubtitleDisplay(
            primaryText = uiState.activeCue?.displayText ?: "",
            secondaryText = uiState.activeCue?.translatedText ?: "",
            settings = uiState.subtitleSettings,
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom=80.dp)
        )

        // Custom controls
        CustomVideoControls(
            isPlaying = uiState.isPlaying,
            positionMs = uiState.positionMs,
            durationMs = uiState.durationMs,
            title = uiState.title,
            onPlayPause = { viewModel.togglePlayPause() },
            onSeek = { viewModel.seekTo(it) },
            onBack = { navController.popBackStack() },
            onPip = onPipEnter,
            onSubtitle = { viewModel.showSubtitleSheet() },
            onSpeed = { viewModel.showSpeedSheet() },
            onMore = { viewModel.showMoreSheet() },
            onNext = { viewModel.next() },
            onPrev = { viewModel.prev() },
            modifier = Modifier.fillMaxSize()
        )

        if(uiState.isLoading){
            VideoLoadingOverlay(message = uiState.loadingMessage)
        }
        if(uiState.showResumeBanner){
            ResumeBanner(positionMs = uiState.resumePosition, onResume = { viewModel.resume() }, onRestart = { viewModel.restart() }, modifier = Modifier.align(Alignment.TopCenter).padding(top=16.dp))
        }
    }

    // Bottom sheets
    if(uiState.showSubtitleSheet){
        SubtitleManagementSheet(cues = uiState.cues, onDismiss = { viewModel.hideSheets() }, onCueClick = { viewModel.seekTo(it.startMs) })
    }
    if(uiState.showSpeedSheet){
        SpeedControlBottomSheet(currentSpeed = uiState.speed, onSpeedChange = { viewModel.setSpeed(it); viewModel.hideSheets() }, onDismiss = { viewModel.hideSheets() })
    }
}
