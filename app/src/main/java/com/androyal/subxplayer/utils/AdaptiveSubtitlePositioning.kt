package com.androyal.subxplayer.utils

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

object AdaptiveSubtitlePositioning {
    fun computeBottomPadding(videoHeightDp: Dp, controlsVisible:Boolean, hasSecondary:Boolean): Dp {
        return when {
            controlsVisible -> 88.dp
            hasSecondary -> 48.dp
            else -> 24.dp
        }
    }
    fun computeTopPadding(forNotch:Boolean): Dp = if(forNotch) 32.dp else 12.dp
}
