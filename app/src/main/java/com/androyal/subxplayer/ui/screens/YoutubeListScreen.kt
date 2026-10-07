package com.androyal.subxplayer.ui.screens


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.navigation.NavController
import com.androyal.subxplayer.network.CatalogRepository
import com.androyal.subxplayer.ui.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YoutubeListScreenScreen(navController: NavController, vm: YoutubeListViewModel = hiltViewModel()){
    val videos by vm.videos.collectAsState()
    val isLoading by vm.isLoading.collectAsState()
    Scaffold(topBar={ TopAppBar(title={Text("YouTube - Learn with TED")}) }){
        padding ->
        if(isLoading) Box(Modifier.padding(padding).fillMaxSize(), contentAlignment=androidx.compose.ui.Alignment.Center){ CircularProgressIndicator()}
        else LazyColumn(Modifier.padding(padding).fillMaxSize(), contentPadding=PaddingValues(16.dp), verticalArrangement=Arrangement.spacedBy(12.dp)){
            items(videos){ v ->
                Card(onClick={ navController.navigate(Screen.YoutubePlayer.create(v.youtubeId)) }, modifier=Modifier.fillMaxWidth()){
                    Column(Modifier.padding(12.dp), verticalArrangement=Arrangement.spacedBy(4.dp)){
                        Text(v.title, style=MaterialTheme.typography.titleMedium)
                        Text("${v.channel} • ${v.duration} • ${v.category}", style=MaterialTheme.typography.bodySmall)
                        Text("Level ${v.level} • ${v.language}", style=MaterialTheme.typography.labelSmall)
                    }
                }
            }
        }
    }
}
class YoutubeListViewModel @javax.inject.Inject constructor(private val repo: CatalogRepository): androidx.lifecycle.ViewModel(){
    private val _videos = kotlinx.coroutines.flow.MutableStateFlow<List<com.androyal.subxplayer.data.models.YoutubeCatalogVideo>>(emptyList())
    val videos: kotlinx.coroutines.flow.StateFlow<List<com.androyal.subxplayer.data.models.YoutubeCatalogVideo>> = _videos
    private val _isLoading = kotlinx.coroutines.flow.MutableStateFlow(true)
    val isLoading: kotlinx.coroutines.flow.StateFlow<Boolean> = _isLoading
    init{
        viewModelScope.launch{
            try{ val c = repo.loadBundledCatalog(); _videos.value = c.videos; }catch(_:Exception){} ; _isLoading.value=false
        }
    }
}
