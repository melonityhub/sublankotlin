
package com.androyal.subxplayer.ui.widgets.browser

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.androyal.subxplayer.data.models.VideoItem

@Composable
fun VideoItemWidget(video: VideoItem, onClick:()->Unit, onLongClick:()->Unit){
    Card(onClick=onClick, modifier=Modifier.fillMaxWidth()){
        Column(Modifier.padding(12.dp)){
            Text(video.displayName, style=MaterialTheme.typography.titleSmall)
            Text(video.uri, style=MaterialTheme.typography.bodySmall)
        }
    }
}
