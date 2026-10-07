
package com.androyal.subxplayer.ui.widgets.browser

import androidx.compose.foundation.clickable
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
import com.androyal.subxplayer.data.models.VideoItem
import com.androyal.subxplayer.utils.FileTypeDetector

@Composable
fun VideoListContent(videos:List<VideoItem>, onVideoClick:(VideoItem)->Unit, onVideoLongPress:(VideoItem)->Unit){
    if(videos.isEmpty()){
        Box(Modifier.fillMaxWidth().padding(32.dp), contentAlignment=Alignment.Center){ Text("No videos found") }
        return
    }
    LazyColumn(verticalArrangement=Arrangement.spacedBy(8.dp), contentPadding=PaddingValues(horizontal=16.dp, vertical=8.dp)){
        items(videos){ v ->
            Card(onClick={onVideoClick(v)}, modifier=Modifier.fillMaxWidth()){
                Row(Modifier.padding(12.dp), verticalAlignment=Alignment.CenterVertically, horizontalArrangement=Arrangement.spacedBy(12.dp)){
                    Card{ Box(Modifier.size(56.dp), contentAlignment=Alignment.Center){ Icon(if(v.isVideo) Icons.Default.Movie else Icons.Default.AudioFile, null)} }
                    Column(Modifier.weight(1f)){
                        Text(v.displayName, maxLines=1, overflow=TextOverflow.Ellipsis, style=MaterialTheme.typography.titleSmall)
                        Text("${formatDuration(v.durationMs)} • ${formatSize(v.sizeBytes)} • ${v.mimeType ?: ""}", style=MaterialTheme.typography.bodySmall)
                        Text(v.folderPath, style=MaterialTheme.typography.labelSmall, maxLines=1, overflow=TextOverflow.Ellipsis)
                    }
                    IconButton(onClick={ onVideoLongPress(v)}){ Icon(Icons.Default.MoreVert,null)}
                }
            }
        }
    }
}
private fun formatDuration(ms:Long):String{
    val s = ms/1000; val m = s/60; val h = m/60
    return if(h>0) String.format("%d:%02d:%02d", h, m%60, s%60) else String.format("%d:%02d", m%60, s%60)
}
private fun formatSize(b:Long):String = when{
    b<1024-> "$b B"; b<1024*1024-> "${b/1024} KB"; b<1024*1024*1024-> String.format("%.1f MB", b/1024f/1024f); else-> String.format("%.2f GB", b/1024f/1024f/1024f)
}
