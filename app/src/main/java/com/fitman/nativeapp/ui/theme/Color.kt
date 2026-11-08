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
            Color(0xD9161F30),
            Color(0xB30B0F19),
            Color(0xCC06080E)
        )
    )

    val GlassInnerSheen = Brush.verticalGradient(
        colors = listOf(
            Color(0x24FFFFFF),
            Color(0x08FFFFFF),
            Color.Transparent
        )
    )

    val GlassCapsuleGradient = Brush.horizontalGradient(
        colors = listOf(
            Color(0xD9101624),
            Color(0xB8090C14),
            Color(0xD9101624)
        )
    )

    val SpecularBorderBrush = Brush.linearGradient(
        colors = listOf(
            Color(0x40FFFFFF),
            Color(0x0DFFFFFF),
            Color(0x08FFFFFF),
            Color(0x26FFFFFF)
        )
    )

    val LimeSpecularBrush = Brush.horizontalGradient(
        colors = listOf(
            Color(0xFFB7FF6A),
            Color(0xFF9BEB4A)
        )
    )

    // Shimmer Skeleton Colors
    val ShimmerBase = Color(0x1A141A29)
    val ShimmerHighlight = Color(0x332E3B54)

    // GameHub Warm Obsidian Theme Tokens (Matched to screenshots)
    val WarmBackground = Color(0xFF0F0D0C)
    val WarmBackgroundTop = Color(0xFF181411)
    val WarmBackgroundBottom = Color(0xFF090807)
    val WarmBackgroundGradient = Brush.verticalGradient(
        listOf(Color(0xFF181411), Color(0xFF0F0D0C), Color(0xFF090807))
    )
    val WarmCardBackground = Color(0xFF221C17)
    val WarmCardDark = Color(0xFF181410)
    val WarmCardBorder = Color(0x336E5642)
    val WarmCardGradient = Brush.verticalGradient(
        listOf(Color(0xFF2A221C), Color(0xFF1D1814))
    )
    val WarmSearchPill = Color(0x662C241E)
    val GoldRank = Color(0xFFC5A059)
    val SilverRank = Color(0xFF7B818C)
    val BronzeRank = Color(0xFFA56E47)
    val StarGold = Color(0xFFE5B85A)
    val TextMutedWarm = Color(0xFF8F8F94)
}
