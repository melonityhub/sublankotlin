
package com.androyal.subxplayer.ui.widgets.player

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.androyal.subxplayer.data.models.SubtitleSettings

@Composable
fun SubtitleDisplay(primaryText:String, secondaryText:String, settings:SubtitleSettings, modifier:Modifier=Modifier){
    if(primaryText.isBlank() && secondaryText.isBlank()) return
    Column(modifier, horizontalAlignment=Alignment.CenterHorizontally, verticalArrangement=Arrangement.spacedBy(4.dp)){
        if(primaryText.isNotBlank()){
            Text(
                primaryText,
                color=Color(settings.textColor),
                fontSize=settings.fontSizeSp.sp,
                textAlign=TextAlign.Center,
                modifier=Modifier.background(Color(settings.backgroundColor), RoundedCornerShape(4.dp)).padding(horizontal=8.dp, vertical=4.dp)
            )
        }
        if(secondaryText.isNotBlank() && settings.secondaryEnabled){
            Text(
                secondaryText,
                color=Color(settings.textColor).copy(alpha=0.9f),
                fontSize=(settings.fontSizeSp*0.85f).sp,
                textAlign=TextAlign.Center,
                modifier=Modifier.background(Color(settings.backgroundColor).copy(alpha=0.7f), RoundedCornerShape(4.dp)).padding(horizontal=8.dp, vertical=2.dp)
            )
        }
    }
}
