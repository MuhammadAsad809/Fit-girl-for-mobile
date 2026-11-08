package com.fitman.nativeapp.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.fitman.nativeapp.ui.theme.FitmanColors

/**
 * Apple iOS 45-inspired Liquid Glass Shimmer Effect.
 * Provides fluid, high-frame-rate skeletal loading for glassmorphic elements.
 */
@Composable
fun rememberLiquidShimmerBrush(): Brush {
    val transition = rememberInfiniteTransition(label = "shimmerTransition")
    val translateAnim by transition.animateFloat(
        initialValue = -800f,
        targetValue = 1600f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1300, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmerTranslate"
    )

    return Brush.linearGradient(
        colors = listOf(
            Color(0x0F192338),
            Color(0x28304260),
            Color(0x40FFFFFF),
            Color(0x28304260),
            Color(0x0F192338)
        ),
        start = Offset(translateAnim, 0f),
        end = Offset(translateAnim + 400f, 300f)
    )
}

@Composable
fun Modifier.liquidGlassShimmer(
    shape: Shape = RoundedCornerShape(20.dp)
): Modifier {
    val shimmerBrush = rememberLiquidShimmerBrush()
    return this
        .clip(shape)
        .background(Color(0x8C090D17))
        .background(shimmerBrush)
        .border(1.dp, FitmanColors.SpecularBorderBrush, shape)
}

@Composable
fun LiquidGlassShimmerHero(
    modifier: Modifier = Modifier
) {
    val heroShape = RoundedCornerShape(26.dp)
    val shimmerBrush = rememberLiquidShimmerBrush()

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(230.dp)
            .clip(heroShape)
            .background(Color(0xA60B0F19))
            .background(shimmerBrush)
            .border(1.dp, FitmanColors.SpecularBorderBrush, heroShape)
            .padding(18.dp)
    ) {
        // Spotlight pill placeholder
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .width(140.dp)
                .height(26.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0x3326344E))
        )

        // Bottom info placeholders
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.65f)
                    .height(20.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0x382A3A56))
            )
            Spacer(Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .width(90.dp)
                        .height(22.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0x2E223048))
                )
                Box(
                    modifier = Modifier
                        .width(100.dp)
                        .height(34.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0x402A3C5C))
                )
            }
        }
    }
}

@Composable
fun LiquidGlassShimmerCard(
    modifier: Modifier = Modifier
) {
    val cardShape = RoundedCornerShape(20.dp)
    val shimmerBrush = rememberLiquidShimmerBrush()

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(cardShape)
            .background(Color(0x800A0E18))
            .background(shimmerBrush)
            .border(1.dp, FitmanColors.SpecularBorderBrush, cardShape)
            .padding(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Artwork placeholder
            Box(
                modifier = Modifier
                    .width(116.dp)
                    .height(72.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0x3326354E))
            )

            Spacer(Modifier.width(14.dp))

            // Metadata lines placeholder
            Column(modifier = Modifier.weight(1f)) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .height(16.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0x3326354E))
                )
                Spacer(Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.50f)
                        .height(13.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0x261E2C42))
                )
                Spacer(Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .width(65.dp)
                        .height(18.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0x2E253652))
                )
            }

            Spacer(Modifier.width(8.dp))

            // Right chevron circle placeholder
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(Color(0x221D293E))
            )
        }
    }
}

@Composable
fun LiquidGlassShimmerList(
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            LiquidGlassShimmerHero()
            Spacer(Modifier.height(6.dp))
        }
        items(4) {
            LiquidGlassShimmerCard()
        }
    }
}
