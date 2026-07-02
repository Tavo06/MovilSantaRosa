package com.upsjb.movilsantarosa.core.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF9FA8DA),
    onPrimary = Color(0xFF0D1448),
    primaryContainer = Color(0xFF1A237E),
    onPrimaryContainer = Color(0xFFE8EAF6),

    secondary = Color(0xFFB0BEC5),
    onSecondary = Color(0xFF263238),
    secondaryContainer = Color(0xFF37474F),
    onSecondaryContainer = Color(0xFFECEFF1),

    tertiary = Color(0xFF4DB6AC),
    onTertiary = Color(0xFF00332E),
    tertiaryContainer = Color(0xFF00574B),
    onTertiaryContainer = Color(0xFFB2DFDB),

    background = Color(0xFF111318),
    onBackground = Color(0xFFE4E6EB),

    surface = Color(0xFF181A20),
    onSurface = Color(0xFFE4E6EB),

    surfaceVariant = Color(0xFF2A2E39),
    onSurfaceVariant = Color(0xFFC4C7CF),

    outline = Color(0xFF90939C)
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF1A237E),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFC5CAE9),
    onPrimaryContainer = Color(0xFF0D1448),

    secondary = Color(0xFF546E7A),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFDCE4E8),
    onSecondaryContainer = Color(0xFF253238),

    tertiary = Color(0xFF00897B),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFB2DFDB),
    onTertiaryContainer = Color(0xFF003D37),

    background = Color(0xFFF8F9FC),
    onBackground = Color(0xFF1A1C1E),

    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF1A1C1E),

    surfaceVariant = Color(0xFFE7EAF3),
    onSurfaceVariant = Color(0xFF45474F),

    outline = Color(0xFF7A7D87)
)

@Composable
fun MovilSantaRosaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}