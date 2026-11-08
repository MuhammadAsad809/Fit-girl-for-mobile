package com.fitman.nativeapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = FitmanColors.ElectricLime,
    onPrimary = Color.Black,
    secondary = FitmanColors.ElectricLimeDark,
    onSecondary = Color.Black,
    tertiary = FitmanColors.TextSecondary,
    background = FitmanColors.MainBackground,
    onBackground = FitmanColors.TextPrimary,
    surface = FitmanColors.SecondaryBackground,
    onSurface = FitmanColors.TextPrimary,
    surfaceVariant = FitmanColors.GlassSurfaceElevated,
    onSurfaceVariant = FitmanColors.TextSecondary
)

@Composable
fun FitmanTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = FitmanTypography,
        content = content
    )
}
