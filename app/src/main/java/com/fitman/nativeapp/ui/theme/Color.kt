package com.fitman.nativeapp.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

object FitmanColors {
    // Core Backgrounds
    val MainBackground = Color(0xFF050608)
    val SecondaryBackground = Color(0xFF090C12)
    val DeepObsidian = Color(0xFF020305)

    // Glass Surfaces (Translucent Black & Graphite)
    val GlassSurface = Color(0x990A0E17)
    val GlassSurfaceSubtle = Color(0x66080B12)
    val GlassSurfaceElevated = Color(0xB3131824)
    val GlassSurfaceOverlay = Color(0xCC06080E)

    // Primary Accents
    val ElectricLime = Color(0xFFB7FF6A)
    val ElectricLimeDark = Color(0xFF88D635)
    val ElectricLimeMuted = Color(0x26B7FF6A)
    val ElectricLimeGlow = Color(0x40B7FF6A)

    // Typography
    val TextPrimary = Color(0xFFFFFFFF)
    val TextSecondary = Color(0xFF9299A5)
    val TextMuted = Color(0xFF5A6270)
    val TextDisabled = Color(0xFF383E49)

    // Hairline Borders & Specular Sheen
    val GlassBorder = Color(0x1FFFFFFF)
    val GlassBorderSubtle = Color(0x12FFFFFF)
    val GlassBorderHighlight = Color(0x3DFFFFFF)
    val SpecularGlint = Color(0x55FFFFFF)

    // Gradients
    val GlassGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xB8161D2B),
            Color(0x990A0E17)
        )
    )

    val GlassCardGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xA6182030),
            Color(0x8C090D15)
        )
    )

    val SpecularBorderBrush = Brush.linearGradient(
        colors = listOf(
            Color(0x38FFFFFF),
            Color(0x0AFFFFFF),
            Color(0x05FFFFFF),
            Color(0x1FFFFFFF)
        )
    )

    val LimeSpecularBrush = Brush.horizontalGradient(
        colors = listOf(
            Color(0xFFB7FF6A),
            Color(0xFF9BEB4A)
        )
    )
}
