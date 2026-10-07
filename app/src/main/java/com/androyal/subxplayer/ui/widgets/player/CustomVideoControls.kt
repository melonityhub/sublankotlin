
package com.androyal.subxplayer.ui.widgets.player

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun CustomVideoControls(
    isPlaying:Boolean,
    positionMs:Long,
    durationMs:Long,
    title:String,
    onPlayPause:()->Unit,
    onSeek:(Long)->Unit,
    onBack:()->Unit,
    onPip:()->Unit,
    onSubtitle:()->Unit,
    onSpeed:()->Unit,
    onMore:()->Unit,
    onNext:()->Unit,
    onPrev:()->Unit,
    modifier:Modifier=Modifier
){
    var showControls by remember{ mutableStateOf(true)}
    Box(modifier.background(Color.Transparent).fillMaxSize()){
        if(showControls){
            // Top bar
            Row(Modifier.align(Alignment.TopCenter).fillMaxWidth().background(Color(0x66000000)).padding(8.dp), verticalAlignment=Alignment.CenterVertically){
                IconButton(onClick=onBack){ Icon(Icons.Default.ArrowBack, contentDescription="Back", tint=Color.White)}
                Text(title, color=Color.White, modifier=Modifier.weight(1f), maxLines=1)
                IconButton(onClick=onPip){ Icon(Icons.Default.PictureInPicture, null, tint=Color.White)}
                IconButton(onClick=onMore){ Icon(Icons.Default.MoreVert, null, tint=Color.White)}
            }
            // Center play
            Row(Modifier.align(Alignment.Center), horizontalArrangement=Arrangement.spacedBy(24.dp), verticalAlignment=Alignment.CenterVertically){
                IconButton(onClick=onPrev){ Icon(Icons.Default.SkipPrevious, null, tint=Color.White, modifier=Modifier.size(36.dp))}
                FilledIconButton(onClick=onPlayPause, modifier=Modifier.size(64.dp)){ Icon(if(isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow, null, modifier=Modifier.size(36.dp))}
                IconButton(onClick=onNext){ Icon(Icons.Default.SkipNext, null, tint=Color.White, modifier=Modifier.size(36.dp))}
            }
            // Bottom
            Column(Modifier.align(Alignment.BottomCenter).fillMaxWidth().background(Color(0x66000000)).padding(12.dp), verticalArrangement=Arrangement.spacedBy(8.dp)){
                MySeekBar(positionMs=positionMs, durationMs=durationMs, onSeek=onSeek)
                Row(horizontalArrangement=Arrangement.spacedBy(12.dp)){
                    IconButton(onClick=onSubtitle){ Icon(Icons.Default.Subtitles, null, tint=Color.White)}
                    IconButton(onClick=onSpeed){ Icon(Icons.Default.Speed, null, tint=Color.White)}
                    Spacer(Modifier.weight(1f))
                    Text("${formatMs(positionMs)} / ${formatMs(durationMs)}", color=Color.White, style=MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}
private fun formatMs(ms:Long):String{
    if(ms<=0) return "0:00"
    val s=ms/1000; val m=s/60; val h=m/60
    return if(h>0) String.format("%d:%02d:%02d",h,m%60,s%60) else String.format("%d:%02d",m%60, s%60)
}
