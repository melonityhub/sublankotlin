
package com.androyal.subxplayer.ui.widgets.player

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

@Composable
fun ReplayButton(onClick:()->Unit, modifier:Modifier=Modifier){
    FilledTonalButton(onClick=onClick, modifier=modifier){ Text("Replay")}
}
