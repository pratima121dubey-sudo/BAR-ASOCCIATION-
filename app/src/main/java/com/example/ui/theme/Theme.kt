package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = GoldPrimary,
    onPrimary = NavyPrimary,
    primaryContainer = NavySecondary,
    onPrimaryContainer = GoldLight,
    secondary = GoldSecondary,
    onSecondary = NavyPrimary,
    secondaryContainer = NavyTertiary,
    onSecondaryContainer = Color.White,
    tertiary = GoldAccent,
    background = DarkBackground,
    onBackground = Color.White,
    surface = DarkCard,
    onSurface = Color.White,
    surfaceVariant = NavySurface,
    onSurfaceVariant = GoldLight
)

private val LightColorScheme = lightColorScheme(
    primary = NavyPrimary,
    onPrimary = Color.White,
    primaryContainer = NavySecondary,
    onPrimaryContainer = GoldLight,
    secondary = GoldAccent,
    onSecondary = NavyPrimary,
    secondaryContainer = GoldLight,
    onSecondaryContainer = NavyPrimary,
    tertiary = NavyTertiary,
    background = LightBackground,
    onBackground = NavyPrimary,
    surface = LightSurface,
    onSurface = NavyPrimary,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = NavyTertiary
)

@Composable
fun BarBalliaTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Keep distinct Navy+Gold branding
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

