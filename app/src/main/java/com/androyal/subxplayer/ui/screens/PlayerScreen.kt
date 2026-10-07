package com.androyal.subxplayer.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.ui.PlayerView
import androidx.navigation.NavController
import com.androyal.subxplayer.ui.widgets.player.*
import com.androyal.subxplayer.ui.widgets.sheets.*


@Composable
fun PlayerScreen(navController: NavController, uriEncoded: String, nameEncoded: String, onPipEnter: () -> Unit) {
    val context = LocalContext.current
    val viewModel: PlayerViewModel = hiltViewModel()
    val uri = Uri.decode(uriEncoded)
    val name = Uri.decode(nameEncoded)
    LaunchedEffect(uri) { viewModel.openUri(Uri.parse(uri), name) }
    val uiState by viewModel.uiState.collectAsState()
    val player = viewModel.playbackManager.getPlayer()
    // Subtitle picker
    val subtitlePicker = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { subUri ->
        if (subUri != null) viewModel.loadSubtitlesFromUri(subUri)
    }

    Box(
        Modifier
            .fillMaxSize()
            .background(Color.Black)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { viewModel.toggleControls() },
                    onDoubleTap = { viewModel.togglePlayPause() }
                )
            }
    ) {
        // Video surface
        AndroidView(
            factory = { ctx ->
                PlayerView(ctx).apply {
                    this.player = player
                    useController = false
                    setKeepContentOnPlayerReset(true)
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        // Gesture overlay placeholder - swipe to seek handled via controls; brightness/volume via system

        // Subtitle display - dual subtitles with styling and gestures
        SubtitleDisplay(
            primaryText = uiState.activeCue?.displayText ?: uiState.activeCue?.text ?: "",
            secondaryText = uiState.activeCue?.translatedText ?: "",
            settings = uiState.subtitleSettings,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 90.dp, start = 16.dp, end = 16.dp)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onLongPress = {
                            // Copy / translate selectable text
                            uiState.activeCue?.let { cue ->
                                // Copy to clipboard handled via sheet
                                viewModel.showSubtitleSheet()
                            }
                        }
                    )
                }
        )

        // Top gradient + controls
        if (uiState.showControls) {
            // Top bar
            Row(
                Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxWidth()
                    .background(Color(0x99000000))
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
                Column(Modifier.weight(1f)) {
                    Text(uiState.title, color = Color.White, style = MaterialTheme.typography.titleSmall, maxLines = 1)
                    if (uiState.cues.isNotEmpty()) Text("${uiState.cues.size} cues • ${uiState.subtitleSettings.primaryLanguage}", color = Color.White.copy(0.7f), style = MaterialTheme.typography.labelSmall)
                }
                IconButton(onClick = onPipEnter) { Icon(Icons.Default.PictureInPicture, null, tint = Color.White) }
                IconButton(onClick = { subtitlePicker.launch(arrayOf("*/*")) }) { Icon(Icons.Default.Subtitles, null, tint = Color.White) }
                IconButton(onClick = { viewModel.showMoreSheet() }) { Icon(Icons.Default.MoreVert, null, tint = Color.White) }
            }

            // Center playback controls
            Box(Modifier.align(Alignment.Center).fillMaxWidth(), contentAlignment = Alignment.Center) {
                Row(horizontalArrangement = Arrangement.spacedBy(24.dp), verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { viewModel.prevCue() }, modifier = Modifier.size(48.dp)) {
                        Icon(Icons.Default.SkipPrevious, null, tint = Color.White, modifier = Modifier.size(36.dp))
                    }
                    FilledIconButton(
                        onClick = { viewModel.togglePlayPause() },
                        modifier = Modifier.size(72.dp),
                        colors = IconButtonDefaults.filledIconButtonColors(containerColor = Color.White, contentColor = Color.Black)
                    ) {
                        Icon(if (uiState.isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow, null, modifier = Modifier.size(40.dp))
                    }
                    IconButton(onClick = { viewModel.nextCue() }, modifier = Modifier.size(48.dp)) {
                        Icon(Icons.Default.SkipNext, null, tint = Color.White, modifier = Modifier.size(36.dp))
                    }
                }
            }

            // Bottom controls
            Column(
                Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .background(Color(0x99000000))
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                MySeekBar(positionMs = uiState.positionMs, durationMs = uiState.durationMs, onSeek = { viewModel.seekTo(it) })
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    IconButton(onClick = { viewModel.showSubtitleSheet() }) { Icon(Icons.Default.List, null, tint = Color.White) }
                    IconButton(onClick = { viewModel.showSpeedSheet() }) { Icon(Icons.Default.Speed, null, tint = Color.White) }
                    // Quick subtitle offset
                    IconButton(onClick = { viewModel.adjustSubtitleOffset(-200) }) { Text("-0.2s", color = Color.White, style = MaterialTheme.typography.labelSmall) }
                    IconButton(onClick = { viewModel.adjustSubtitleOffset(200) }) { Text("+0.2s", color = Color.White, style = MaterialTheme.typography.labelSmall) }
                    Spacer(Modifier.weight(1f))
                    if (uiState.autoPause) Badge(containerColor = MaterialTheme.colorScheme.primary) { Text("AP", modifier = Modifier.padding(horizontal = 4.dp)) }
                    if (uiState.autoSkip) Badge(containerColor = MaterialTheme.colorScheme.tertiary) { Text("AS", modifier = Modifier.padding(horizontal = 4.dp)) }
                    if (uiState.autoRepeat) Badge(containerColor = MaterialTheme.colorScheme.secondary) { Text("AR x${uiState.autoRepeatCount}", modifier = Modifier.padding(horizontal = 4.dp)) }
                    Text("${formatMs(uiState.positionMs)} / ${formatMs(uiState.durationMs)}", color = Color.White, style = MaterialTheme.typography.bodySmall)
                }
                // Extra row: dual subtitle toggle, auto modes
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                    FilterChip(selected = uiState.autoPause, onClick = { viewModel.toggleAutoPause() }, label = { Text("Auto Pause") }, leadingIcon = { Icon(Icons.Default.Pause, null, modifier = Modifier.size(16.dp)) })
                    FilterChip(selected = uiState.autoSkip, onClick = { viewModel.toggleAutoSkip() }, label = { Text("Auto Skip") })
                    FilterChip(selected = uiState.autoRepeat, onClick = { viewModel.toggleAutoRepeat() }, label = { Text("Auto Repeat") })
                    FilterChip(selected = uiState.subtitleListVisible, onClick = { }, label = { Text("Sub List") })
                }
            }
        } else {
            // Tap hint when controls hidden
            Box(Modifier.align(Alignment.BottomCenter).padding(bottom = 16.dp).background(Color(0x66000000), shape = MaterialTheme.shapes.small).padding(6.dp)) {
                Text("Tap to show controls • Double tap play/pause • Swipe to seek", color = Color.White, style = MaterialTheme.typography.labelSmall)
            }
        }

        if (uiState.isBuffering) {
            Box(Modifier.align(Alignment.Center)) {
                CircularProgressIndicator(color = Color.White)
            }
        }

        if (uiState.isLoading) {
            VideoLoadingOverlay(message = uiState.loadingMessage, modifier = Modifier.align(Alignment.Center))
        }

        if (uiState.showResumeBanner) {
            ResumeBanner(
                positionMs = uiState.resumePosition,
                onResume = { viewModel.resume() },
                onRestart = { viewModel.restart() },
                modifier = Modifier.align(Alignment.TopCenter).padding(top = 72.dp, start = 16.dp, end = 16.dp)
            )
        }

        uiState.error?.let { err ->
            Snackbar(
                modifier = Modifier.align(Alignment.BottomCenter).padding(16.dp).padding(bottom = if (uiState.showControls) 140.dp else 40.dp),
                action = { TextButton(onClick = { viewModel.clearError() }) { Text("Dismiss") } },
                containerColor = MaterialTheme.colorScheme.errorContainer,
                contentColor = MaterialTheme.colorScheme.onErrorContainer
            ) { Text(err) }
        }
    }

    // Bottom sheets
    if (uiState.showSubtitleSheet) {
        SubtitleManagementSheet(
            cues = uiState.cues,
            activeIndex = uiState.activeCueIndex,
            onDismiss = { viewModel.hideSheets() },
            onCueClick = { cue -> viewModel.seekToCue(cue); viewModel.hideSheets() },
            onCueEdit = { cue -> viewModel.editCue(cue) },
            onCueDelete = { cue -> viewModel.deleteCue(cue) },
            onAnkiExport = { cue -> viewModel.exportToAnki(cue) },
            onPickFile = { subtitlePicker.launch(arrayOf("text/*", "application/*")) },
            onGenerate = { viewModel.generateSubtitles() }
        )
    }
    if (uiState.showSpeedSheet) {
        SpeedControlBottomSheet(currentSpeed = uiState.speed, onSpeedChange = { viewModel.setSpeed(it); viewModel.hideSheets() }, onDismiss = { viewModel.hideSheets() })
    }
    if (uiState.showMoreSheet) {
        PlayerMoreSheet(
            onDismiss = { viewModel.hideSheets() },
            onExportSrt = { viewModel.exportSubtitles("srt") { msg -> viewModel.clearError(); /* show snack */ } },
            onExportVtt = { viewModel.exportSubtitles("vtt") { } },
            onExportTxt = { viewModel.exportSubtitles("txt") { } },
            onStyling = { /* open styling */ },
            subtitleSettings = uiState.subtitleSettings,
            onSettingsChange = { viewModel.updateSubtitleSettings(it) }
        )
    }
    if (uiState.showEditDialog && uiState.editingCue != null) {
        SubtitleEditDialog(
            cue = uiState.editingCue!!,
            onDismiss = { viewModel.hideSheets() },
            onSave = { txt, start, end -> viewModel.saveEditedCue(txt, start, end) }
        )
    }
}

private fun formatMs(ms: Long): String {
    if (ms <= 0) return "0:00"
    val s = ms / 1000; val m = s / 60; val h = m / 60
    return if (h > 0) String.format("%d:%02d:%02d", h, m % 60, s % 60) else String.format("%d:%02d", m % 60, s % 60)
}
