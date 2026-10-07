
package com.androyal.subxplayer.ui.widgets.custom

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PremiumFeatureUpsellSheet(feature:String, onUpgrade:()->Unit, onDismiss:()->Unit){
    AlertDialog(
        onDismissRequest=onDismiss,
        title={ Text("SubX+ required")},
        text={ Text(feature + " is a premium feature. Upgrade to unlock it and support development.")},
        confirmButton={ Button(onClick=onUpgrade){ Text("Upgrade")}},
        dismissButton={ TextButton(onClick=onDismiss){ Text("Later")}}
    )
}

@Composable
fun PremiumFeatureEnableDialog(feature:String, onEnable:()->Unit, onDismiss:()->Unit){
    AlertDialog(onDismissRequest=onDismiss, title={Text("Enable " + feature + "?")}, text={Text("This will enable " + feature + ". Premium may be required for some modes.")}, confirmButton={Button(onClick=onEnable){Text("Enable")}}, dismissButton={TextButton(onClick=onDismiss){Text("Cancel")}})
}
