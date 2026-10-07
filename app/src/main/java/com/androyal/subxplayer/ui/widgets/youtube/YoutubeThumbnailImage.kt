
package com.androyal.subxplayer.ui.widgets.youtube

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun YoutubeThumbnailImage(youtubeId:String, modifier:Modifier=Modifier){
    AsyncImage(model="https://img.youtube.com/vi/"+youtubeId+"/hqdefault.jpg", contentDescription=null, modifier=modifier, contentScale=ContentScale.Crop)
}
