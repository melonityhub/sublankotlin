
package com.androyal.subxplayer.ui.widgets.sheets
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BilingualAudioDelayBottomSheet(onDismiss:()->Unit){ ModalBottomSheet(onDismissRequest=onDismiss){ Column(Modifier.padding(16.dp)){ Text("BilingualAudioDelayBottomSheet") ; Button(onClick=onDismiss){ Text("Close") } } } }
