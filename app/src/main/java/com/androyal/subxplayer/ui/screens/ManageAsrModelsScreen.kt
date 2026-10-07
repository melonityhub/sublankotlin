package com.androyal.subxplayer.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.androyal.subxplayer.features.subtitlegeneration.AsrModelCatalog
import com.androyal.subxplayer.utils.ModelDownloadManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ManageAsrViewModel @Inject constructor(
    val downloadManager: ModelDownloadManager
) : ViewModel()

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageAsrModelsScreen(navController: NavController, vm: ManageAsrViewModel = hiltViewModel()) {
    val states by vm.downloadManager.states.collectAsState()
    val scope = rememberCoroutineScope()
    var message by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("ASR Models — Offline AI") },
                navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Text("←") } }
            )
        }
    ) { padding ->
        LazyColumn(
            Modifier.padding(padding).fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer), modifier = Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text("Offline Subtitle Generation", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSecondaryContainer)
                        Text("Generate & translate subtitles fully offline and private on-device. Choose fast or accurate models. Fully works without internet after download.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSecondaryContainer)
                        Text("If download fails, check your internet or the catalog Base URL in Settings > Secrets. Mirrors are tried automatically.", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSecondaryContainer)
                    }
                }
            }
            items(AsrModelCatalog.models) { m ->
                val st = states[m.id]
                val downloaded = vm.downloadManager.isModelDownloaded(m.id)
                val isDownloading = st?.isDownloading == true
                val progress = st?.progress ?: 0
                val error = st?.error

                Card(Modifier.fillMaxWidth(), elevation = CardDefaults.cardElevation(2.dp)) {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Column(Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    Text(m.displayName, style = MaterialTheme.typography.titleMedium)
                                    if (downloaded) {
                                        Icon(Icons.Default.CheckCircle, contentDescription = "Downloaded", modifier = Modifier.size(18.dp), tint = MaterialTheme.colorScheme.primary)
                                    }
                                }
                                Text("${m.language} • ${m.sizeMb} MB • ${m.type}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                if (m.files.isNotEmpty()) {
                                    Text(m.files.first().fileName, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }
                            Badge(containerColor = if (downloaded) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant) {
                                Text(if (downloaded) "Ready" else "Not downloaded", modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                            }
                        }

                        if (isDownloading) {
                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                LinearProgressIndicator(progress = { progress / 100f }, modifier = Modifier.fillMaxWidth())
                                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text("Downloading $progress% ${st?.bytesDownloaded?.let { formatBytes(it) } ?: ""}", style = MaterialTheme.typography.labelSmall)
                                    if (st?.totalBytes != null && st.totalBytes > 0) Text(formatBytes(st.totalBytes), style = MaterialTheme.typography.labelSmall)
                                }
                            }
                        }
                        if (error != null) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Icon(Icons.Default.Error, null, tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(16.dp))
                                Text(error, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.error, modifier = Modifier.weight(1f))
                            }
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                            if (downloaded) {
                                OutlinedButton(
                                    onClick = {
                                        scope.launch {
                                            vm.downloadManager.delete(m.id)
                                            message = "${m.displayName} deleted"
                                        }
                                    },
                                    modifier = Modifier.weight(1f)
                                ) { Text("Delete") }
                                Button(
                                    onClick = { message = "${m.displayName} is ready for offline transcription" },
                                    modifier = Modifier.weight(1f)
                                ) { Text("Use") }
                            } else {
                                Button(
                                    onClick = {
                                        scope.launch {
                                            val files = m.files.map { it.url to it.fileName }
                                            val res = if (files.size == 1) {
                                                vm.downloadManager.download(m.id, files[0].first, files[0].second)
                                            } else {
                                                vm.downloadManager.downloadMultiple(m.id, files)
                                            }
                                            message = if (res.isSuccess) "Downloaded ${m.displayName}" else "Failed: ${res.exceptionOrNull()?.message}"
                                        }
                                    },
                                    enabled = !isDownloading,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    if (isDownloading) {
                                        CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                                        Spacer(Modifier.width(6.dp))
                                        Text("$progress%")
                                    } else {
                                        Icon(Icons.Default.Download, null, modifier = Modifier.size(16.dp))
                                        Spacer(Modifier.width(6.dp))
                                        Text("Download")
                                    }
                                }
                                OutlinedButton(onClick = { message = "Info: ${m.displayName} • ${m.language} • ${m.sizeMb}MB" }, modifier = Modifier.weight(1f)) { Text("Info") }
                            }
                        }
                    }
                }
            }
            item {
                message?.let {
                    Card(Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
                        Text(it, Modifier.padding(12.dp), style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}

private fun formatBytes(b: Long): String = when {
    b < 1024 -> "$b B"
    b < 1024 * 1024 -> "${b / 1024} KB"
    b < 1024 * 1024 * 1024 -> String.format("%.1f MB", b / 1024f / 1024f)
    else -> String.format("%.2f GB", b / 1024f / 1024f / 1024f)
}
