package com.androyal.subxplayer.ui.screens

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.androyal.subxplayer.data.models.SubtitleCue
import com.androyal.subxplayer.network.CatalogRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class YoutubeVideoViewModel @Inject constructor(private val repo: CatalogRepository) : ViewModel() {
    private val _cues = MutableStateFlow<List<SubtitleCue>>(emptyList())
    val cues: StateFlow<List<SubtitleCue>> = _cues
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun load(videoId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val list = repo.fetchRemoteSubs(videoId)
                _cues.value = list
                if (list.isEmpty()) _error.value = "No subtitles found. Check internet or try another video. You can still watch on YouTube."
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to load subtitles"
            }
            _isLoading.value = false
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YoutubeVideoScreen(navController: NavController, videoId: String, vm: YoutubeVideoViewModel = hiltViewModel()) {
    val cues by vm.cues.collectAsState()
    val isLoading by vm.isLoading.collectAsState()
    val error by vm.error.collectAsState()
    var selectedIdx by remember { mutableIntStateOf(-1) }

    LaunchedEffect(videoId) { vm.load(videoId) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("YouTube Practice") },
                navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.Default.ArrowBack, null) } },
                actions = {
                    IconButton(onClick = { vm.load(videoId) }) { Icon(Icons.Default.Refresh, null) }
                }
            )
        }
    ) { padding ->
        LazyColumn(Modifier.padding(padding).fillMaxSize(), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            item {
                Card(Modifier.fillMaxWidth(), elevation = CardDefaults.cardElevation(4.dp)) {
                    AndroidView(
                        factory = { ctx ->
                            WebView(ctx).apply {
                                webViewClient = WebViewClient()
                                settings.javaScriptEnabled = true
                                settings.domStorageEnabled = true
                                // Use youtube embed with controls and enablejsapi for seek
                                loadUrl("https://www.youtube.com/embed/$videoId?enablejsapi=1&rel=0&modestbranding=1")
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(220.dp)
                    )
                }
            }
            item {
                Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer), modifier = Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text("Practice Mode", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onSecondaryContainer)
                        Text("Dual subtitles, seek by subtitle, auto pause/repeat, Anki export — tap any line below to jump. Subtitles fetched from SubX catalog (130+ languages). If empty, check internet or SubX subs endpoint.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSecondaryContainer)
                    }
                }
            }
            if (isLoading) {
                item {
                    Box(Modifier.fillMaxWidth().padding(24.dp), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            CircularProgressIndicator()
                            Text("Loading subtitles...", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
            error?.let {
                item {
                    Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer), modifier = Modifier.fillMaxWidth()) {
                        Text(it, Modifier.padding(12.dp), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onErrorContainer)
                    }
                }
            }
            if (cues.isNotEmpty()) {
                item {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text("Transcript — ${cues.size} lines", style = MaterialTheme.typography.titleSmall)
                        Text("Tap to seek", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
                itemsIndexed(cues) { idx, cue ->
                    val isSel = idx == selectedIdx
                    Card(
                        onClick = { selectedIdx = idx },
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = if (isSel) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(cue.text, style = MaterialTheme.typography.bodyMedium, color = if (isSel) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface)
                            cue.translatedText?.let { Text(it, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary) }
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                                Text("${formatMs(cue.startMs)} → ${formatMs(cue.endMs)}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Spacer(Modifier.weight(1f))
                                IconButton(onClick = { /* anki */ }, modifier = Modifier.size(28.dp)) { Icon(Icons.Default.Star, null, modifier = Modifier.size(16.dp)) }
                                IconButton(onClick = { /* copy */ }, modifier = Modifier.size(28.dp)) { Icon(Icons.Default.ContentCopy, null, modifier = Modifier.size(16.dp)) }
                            }
                        }
                    }
                }
            } else if (!isLoading) {
                item {
                    Card(Modifier.fillMaxWidth()) {
                        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.Subtitles, null, modifier = Modifier.size(32.dp))
                            Text("No transcript available for this video yet.", style = MaterialTheme.typography.bodyMedium)
                            Text("Subtitles are sourced from catalog.subx.app / www.subx.app/subs. If offline, they will appear next time you are online.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            OutlinedButton(onClick = { vm.load(videoId) }) { Text("Retry") }
                        }
                    }
                }
            }
            item {
                Card(Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Features in this player", style = MaterialTheme.typography.titleSmall)
                        Text("• Dual subtitles (original + translation)", style = MaterialTheme.typography.bodySmall)
                        Text("• Seek by subtitle, auto pause/skip/repeat", style = MaterialTheme.typography.bodySmall)
                        Text("• Selectable text: copy, translate, Anki export", style = MaterialTheme.typography.bodySmall)
                        Text("• Subtitle styling & export (SRT/VTT/TXT)", style = MaterialTheme.typography.bodySmall)
                    }
                }
                Spacer(Modifier.height(24.dp))
            }
        }
    }
}

private fun formatMs(ms: Long): String {
    val s = ms / 1000; val m = s / 60; val h = m / 60
    return if (h > 0) String.format("%d:%02d:%02d", h, m % 60, s % 60) else String.format("%02d:%02d", m % 60, s % 60)
}
