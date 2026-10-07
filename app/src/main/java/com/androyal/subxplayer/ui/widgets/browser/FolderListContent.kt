
package com.androyal.subxplayer.ui.widgets.browser

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.androyal.subxplayer.data.models.FolderEntry

@Composable
fun FolderListContent(folders:List<FolderEntry>, onFolderClick:(FolderEntry)->Unit, onPlayFolder:(FolderEntry)->Unit){
    if(folders.isEmpty()) return
    LazyRow(contentPadding=PaddingValues(horizontal=16.dp, vertical=8.dp), horizontalArrangement=Arrangement.spacedBy(12.dp)){
        items(folders){ f ->
            Card(onClick={onFolderClick(f)}, modifier=Modifier.width(180.dp)){
                Column(Modifier.padding(12.dp), verticalArrangement=Arrangement.spacedBy(8.dp)){
                    Icon(Icons.Default.Folder, contentDescription=null, tint=MaterialTheme.colorScheme.primary)
                    Text(f.name, style=MaterialTheme.typography.titleSmall, maxLines=1)
                    Text("${f.videoCount} videos", style=MaterialTheme.typography.bodySmall)
                    OutlinedButton(onClick={onPlayFolder(f)}, modifier=Modifier.fillMaxWidth()){ Text("Play all")}
                }
            }
        }
    }
}
