
package com.androyal.subxplayer.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily

// SubX brand: deep indigo + teal accent, replicating original Flutter Material3 seed
private val LightSeed = Color(0xFF4F46E5) // indigo
private val DarkSeed = Color(0xFF6366F1)

private val LightScheme = lightColorScheme(
    primary = Color(0xFF4F46E5),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE0E7FF),
    secondary = Color(0xFF06B6D4),
    secondaryContainer = Color(0xFFCFFAFE),
    tertiary = Color(0xFF8B5CF6),
    background = Color(0xFFFCFCFF),
    surface = Color(0xFFFCFCFF),
    surfaceVariant = Color(0xFFE8E8F0),
    error = Color(0xFFDC2626),
)

private val DarkScheme = darkColorScheme(
    primary = Color(0xFF818CF8),
    onPrimary = Color(0xFF1E1B4B),
    primaryContainer = Color(0xFF3730A3),
    secondary = Color(0xFF22D3EE),
    secondaryContainer = Color(0xFF164E63),
    tertiary = Color(0xFFA78BFA),
    background = Color(0xFF0F0F14),
    surface = Color(0xFF121218),
    surfaceVariant = Color(0xFF23232B),
    error = Color(0xFFF87171),
)

@Composable
fun SubXTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val scheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkScheme
        else -> LightScheme
    }
    MaterialTheme(
        colorScheme = scheme,
        typography = Typography(),
        content = content
    )
}

// Chrome tokens mirroring ui/theme/settings_chrome_tokens.dart
object SettingsChromeTokens {
    val cardShape = androidx.compose.foundation.shape.RoundedCornerShape(16)
    val sectionSpacing = 16.dp
}
