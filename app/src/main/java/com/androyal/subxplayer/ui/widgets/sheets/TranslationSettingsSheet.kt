
package com.androyal.subxplayer.ui.widgets.sheets

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TranslationSettingsSheet(onDismiss:()->Unit){
    ModalBottomSheet(onDismissRequest=onDismiss){
        Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement=Arrangement.spacedBy(12.dp)){
            Text("TranslationSettings", style=MaterialTheme.typography.titleLarge)
            Text("Target language, auto-translate, overlay style, offline packs.", style=MaterialTheme.typography.bodyMedium)
            Button(onClick=onDismiss, modifier=Modifier.fillMaxWidth()){ Text("Close") }
        }
    }
}
