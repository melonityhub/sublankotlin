package com.androyal.subxplayer.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.androyal.subxplayer.network.CatalogRepository
import com.androyal.subxplayer.ui.navigation.Screen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YoutubeListScreen(navController: NavController, vm: YoutubeListViewModel = hiltViewModel()) {
    val videos by vm.videos.collectAsState()
    val isLoading by vm.isLoading.collectAsState()
    var query by remember { mutableStateOf("") }
    var levelFilter by remember { mutableStateOf<Int?>(null) }

    val filtered = remember(videos, query, levelFilter) {
        var list = videos
        if (query.isNotBlank()) list = list.filter { it.title.contains(query, true) || it.channel.contains(query, true) || it.category.contains(query, true) }
        if (levelFilter != null) list = list.filter { it.level == levelFilter }
        list
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Practice Mode — YouTube") },
                navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.Default.ArrowBack, null) } }
            )
        }
    ) { padding ->
        Column(Modifier.padding(padding).fillMaxSize()) {
            // Header card
            Card(Modifier.padding(16.dp).fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("Learn with curated TED Talks + dual subtitles", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onPrimaryContainer)
                    Text("Tap any video to practice. Dual subtitles, seek by subtitle, auto pause/repeat, Anki export — all available in YouTube practice mode.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onPrimaryContainer)
                }
            }
            // Search + filters
            Row(Modifier.padding(horizontal = 16.dp).fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it },
                    placeholder = { Text("Search TED talks") },
                    leadingIcon = { Icon(Icons.Default.Search, null) },
                    trailingIcon = { if (query.isNotEmpty()) IconButton(onClick = { query = "" }) { Icon(Icons.Default.Clear, null) } },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
            }
            Spacer(Modifier.height(8.dp))
            Row(Modifier.padding(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(selected = levelFilter == null, onClick = { levelFilter = null }, label = { Text("All levels") })
                FilterChip(selected = levelFilter == 1, onClick = { levelFilter = 1 }, label = { Text("L1") })
                FilterChip(selected = levelFilter == 2, onClick = { levelFilter = 2 }, label = { Text("L2") })
                FilterChip(selected = levelFilter == 3, onClick = { levelFilter = 3 }, label = { Text("L3") })
            }
            Spacer(Modifier.height(8.dp))
            if (isLoading) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        CircularProgressIndicator()
                        Text("Loading catalog...", style = MaterialTheme.typography.bodySmall)
                    }
                }
            } else if (filtered.isEmpty()) {
                Box(Modifier.fillMaxSize().padding(32.dp), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Icon(Icons.Default.SearchOff, null, modifier = Modifier.size(48.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(if (videos.isEmpty()) "No videos in catalog.\nCheck internet or bundled assets." else "No results for \"$query\"", style = MaterialTheme.typography.bodyMedium)
                        if (videos.isEmpty()) Button(onClick = { vm.reload() }) { Text("Retry") }
                    }
                }
            } else {
                LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(filtered) { v ->
                        Card(onClick = { navController.navigate(Screen.YoutubePlayer.create(v.youtubeId)) }, modifier = Modifier.fillMaxWidth(), elevation = CardDefaults.cardElevation(2.dp)) {
                            Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                Text(v.title, style = MaterialTheme.typography.titleSmall, maxLines = 2, overflow = TextOverflow.Ellipsis)
                                Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                                    Badge(containerColor = when (v.level) { 1 -> MaterialTheme.colorScheme.primary; 2 -> MaterialTheme.colorScheme.tertiary; else -> MaterialTheme.colorScheme.secondary }) {
                                        Text("L${v.level}", modifier = Modifier.padding(horizontal = 6.dp))
                                    }
                                    Text("${v.channel} • ${v.duration} • ${v.category}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    AssistChip(onClick = {}, label = { Text(v.language.uppercase()) }, leadingIcon = { Icon(Icons.Default.Language, null, modifier = Modifier.size(14.dp)) })
                                    AssistChip(onClick = {}, label = { Text("Dual Subs") }, leadingIcon = { Icon(Icons.Default.Subtitles, null, modifier = Modifier.size(14.dp)) })
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

class YoutubeListViewModel @javax.inject.Inject constructor(private val repo: CatalogRepository) : androidx.lifecycle.ViewModel() {
    private val _videos = kotlinx.coroutines.flow.MutableStateFlow<List<com.androyal.subxplayer.data.models.YoutubeCatalogVideo>>(emptyList())
    val videos: kotlinx.coroutines.flow.StateFlow<List<com.androyal.subxplayer.data.models.YoutubeCatalogVideo>> = _videos
    private val _isLoading = kotlinx.coroutines.flow.MutableStateFlow(true)
    val isLoading: kotlinx.coroutines.flow.StateFlow<Boolean> = _isLoading

    init { load() }

    private fun load() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val c = repo.loadBundledCatalog()
                _videos.value = c.videos
                // Also try to fetch remote catalog silently to update if needed
                // For now bundled is enough
            } catch (e: Exception) {
                android.util.Log.w("YoutubeList", "load failed", e)
            }
            _isLoading.value = false
        }
    }

    fun reload() = load()
}
