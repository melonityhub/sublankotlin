
package com.androyal.subxplayer.ui.widgets.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SettingsSection(title:String, content: @Composable ColumnScope.()->Unit){
    Column(verticalArrangement=Arrangement.spacedBy(8.dp)){
        Text(title, style=MaterialTheme.typography.titleSmall, color=MaterialTheme.colorScheme.primary, modifier=Modifier.padding(horizontal=4.dp))
        Card{ Column(Modifier.fillMaxWidth(), verticalArrangement=Arrangement.spacedBy(0.dp)){ content() } }
    }
}

@Composable
fun SettingsRow(title:String, subtitle:String? = null, value:String? = null, trailing: @Composable (() -> Unit)? = null, onClick:(()->Unit)? = null){
    val mod = if(onClick!=null) Modifier.clickable{onClick()} else Modifier
    Row(mod.then(Modifier.fillMaxWidth().padding(16.dp)), verticalAlignment=Alignment.CenterVertically, horizontalArrangement=Arrangement.SpaceBetween){
        Column(Modifier.weight(1f)){
            Text(title, style=MaterialTheme.typography.bodyLarge)
            if(subtitle!=null) Text(subtitle, style=MaterialTheme.typography.bodySmall, color=MaterialTheme.colorScheme.onSurfaceVariant)
            if(value!=null) Text(value, style=MaterialTheme.typography.bodySmall, color=MaterialTheme.colorScheme.primary)
        }
        if(trailing!=null) trailing() else if(onClick!=null) Icon(Icons.Default.ChevronRight, null, tint=MaterialTheme.colorScheme.onSurfaceVariant)
    }
    if(trailing==null && onClick!=null) HorizontalDivider(modifier=Modifier.padding(horizontal=16.dp))
}

@Composable
fun SettingsGroup(title:String, content:@Composable ()->Unit){}
@Composable
fun SettingsNavChevron(){ Icon(Icons.Default.ChevronRight, null)}
