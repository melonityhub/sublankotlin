
package com.androyal.subxplayer.ui.widgets.player

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun VideoLoadingOverlay(message:String, modifier:Modifier = Modifier){
    Box(modifier.fillMaxSize().background(Color(0x88000000)), contentAlignment=Alignment.Center){
        Column(horizontalAlignment=Alignment.CenterHorizontally, verticalArrangement=Arrangement.spacedBy(12.dp)){
            CircularProgressIndicator(color=Color.White)
            Text(message, color=Color.White)
        }
    }
}
