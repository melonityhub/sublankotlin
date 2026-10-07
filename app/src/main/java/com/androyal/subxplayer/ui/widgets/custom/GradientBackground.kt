
package com.androyal.subxplayer.ui.widgets.custom

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.MaterialTheme

@Composable
fun GradientBackground(content:@Composable ()->Unit){
    val colors = listOf(MaterialTheme.colorScheme.surface, MaterialTheme.colorScheme.surfaceVariant.copy(alpha=0.3f))
    Box(Modifier.fillMaxSize().background(Brush.verticalGradient(colors))){ content() }
}
