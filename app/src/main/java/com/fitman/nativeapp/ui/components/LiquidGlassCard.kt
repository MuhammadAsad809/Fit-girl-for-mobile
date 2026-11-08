package com.fitman.nativeapp.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.fitman.nativeapp.ui.theme.FitmanColors

@Composable
fun LiquidGlassCard(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(22.dp),
    borderBrush: Brush = FitmanColors.SpecularBorderBrush,
    backgroundBrush: Brush = FitmanColors.GlassCardGradient,
    borderWidth: Dp = 1.dp,
    elevation: Dp = 8.dp,
    showTopHighlight: Boolean = true,
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed && onClick != null) 0.975f else 1.0f,
        animationSpec = spring(dampingRatio = 0.72f, stiffness = 450f),
        label = "glassCardScale"
    )

    var cardModifier = modifier
        .scale(scale)
        .shadow(
            elevation = elevation,
            shape = shape,
            ambientColor = Color.Black.copy(alpha = 0.65f),
            spotColor = Color.Black.copy(alpha = 0.85f)
        )
        .clip(shape)
        .background(brush = backgroundBrush)
        .border(width = borderWidth, brush = borderBrush, shape = shape)

    if (onClick != null) {
        cardModifier = cardModifier
            .pointerInput(Unit) {
                awaitEachGesture {
                    awaitFirstDown()
                    isPressed = true
                    val up = waitForUpOrCancellation()
                    isPressed = false
                    if (up != null) {
                        onClick()
                    }
                }
            }
    }

    Box(modifier = cardModifier) {
        // Soft optical inner sheen (subtle refraction depth)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(46.dp)
                .background(FitmanColors.GlassInnerSheen)
        )

        // Specular top edge glint (iOS 45 liquid glass refraction simulation)
        if (showTopHighlight) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color.Transparent,
                                FitmanColors.GlassBorderHighlight.copy(alpha = 0.3f),
                                FitmanColors.SpecularGlint,
                                FitmanColors.GlassBorderHighlight.copy(alpha = 0.3f),
                                Color.Transparent
                            )
                        )
                    )
            )
        }

        content()
    }
}
