
package com.androyal.subxplayer.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SecretsConfigDialog(
    catalogUrl:String,
    revenueCatKey:String,
    subsUrl:String,
    firebaseKey:String,
    onDismiss:()->Unit,
    onSave:(String,String,String,String)->Unit
){
    var catalog by remember{ mutableStateOf(catalogUrl)}
    var rc by remember{ mutableStateOf(revenueCatKey)}
    var subs by remember{ mutableStateOf(subsUrl)}
    var fb by remember{ mutableStateOf(firebaseKey)}
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Secrets & Endpoints")},
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)){
                Text("Leave empty to use defaults. Restart app after changing.", style=MaterialTheme.typography.bodySmall)
                OutlinedTextField(value=catalog, onValueChange={catalog=it}, label={Text("Catalog base URL")}, singleLine=true, modifier=Modifier.fillMaxWidth())
                OutlinedTextField(value=rc, onValueChange={rc=it}, label={Text("RevenueCat Google key")}, singleLine=true, modifier=Modifier.fillMaxWidth())
                OutlinedTextField(value=subs, onValueChange={subs=it}, label={Text("Subs base URL")}, singleLine=true, modifier=Modifier.fillMaxWidth())
                OutlinedTextField(value=fb, onValueChange={fb=it}, label={Text("Firebase API key")}, singleLine=true, modifier=Modifier.fillMaxWidth())
            }
        },
        confirmButton = { TextButton(onClick={ onSave(catalog, rc, subs, fb)}){ Text("Save")} },
        dismissButton = { TextButton(onClick=onDismiss){ Text("Cancel")} }
    )
}
