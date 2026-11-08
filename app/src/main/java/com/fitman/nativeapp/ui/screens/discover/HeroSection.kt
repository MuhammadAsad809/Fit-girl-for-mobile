package com.fitman.nativeapp.ui.screens.discover

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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.fitman.nativeapp.data.model.Game
import com.fitman.nativeapp.ui.components.LiquidGlassButton
import com.fitman.nativeapp.ui.components.LiquidGlassCard
import com.fitman.nativeapp.ui.theme.FitmanColors

/**
 * Apple iOS 45-inspired Liquid Glass Cinematic Hero Section.
 * Features multi-layer obsidian gradients, pulsing specular spotlight badge,
 * dynamic pricing pill, and tactile spring CTA.
 */
@Composable
fun HeroSection(
    game: Game,
    onExplore: (Game) -> Unit,
    modifier: Modifier = Modifier
) {
    val heroShape = RoundedCornerShape(26.dp)

    // Pulsing dot animation for live spotlight status
    val infiniteTransition = rememberInfiniteTransition(label = "heroPulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 900, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "spotlightPulse"
    )

    LiquidGlassCard(
        modifier = modifier
            .fillMaxWidth()
            .height(240.dp),
        shape = heroShape,
        elevation = 16.dp,
        onClick = { onExplore(game) }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Background Artwork
            AsyncImage(
                model = game.image,
                contentDescription = game.title,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(heroShape),
                contentScale = ContentScale.Crop
            )

            // Deep obsidian gradient overlay with multi-stop optical glass curve
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color(0x33050608),
                                Color(0x66050608),
                                Color(0xD9050608),
                                Color(0xFD050608)
                            )
                        )
                    )
            )

            // Top Specular Badges Row
            Row(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .fillMaxWidth()
                    .padding(14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Spotlight Badge with Pulsing Green Dot
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0xB3000000))
                        .border(1.dp, FitmanColors.GlassBorderHighlight.copy(alpha = 0.4f), RoundedCornerShape(14.dp))
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(7.dp)
                                .clip(CircleShape)
                                .background(FitmanColors.ElectricLime.copy(alpha = pulseAlpha))
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            text = "SPOTLIGHT",
                            color = FitmanColors.ElectricLime,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.5.sp
                        )
                    }
                }

                // Discount or Tag Pill (if available)
                if (game.discountPercent > 0) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(FitmanColors.ElectricLimeMuted)
                            .border(1.dp, FitmanColors.ElectricLime.copy(alpha = 0.6f), RoundedCornerShape(12.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.LocalOffer,
                                contentDescription = null,
                                tint = FitmanColors.ElectricLime,
                                modifier = Modifier.size(11.dp)
                            )
                            Spacer(Modifier.width(4.dp))
                            Text(
                                text = "-${game.discountPercent}%",
                                color = FitmanColors.ElectricLime,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    }
                }
            }

            // Bottom Content Information
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 12.dp)
                    ) {
                        Text(
                            text = game.title,
                            color = FitmanColors.TextPrimary,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        Spacer(Modifier.height(6.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            // Floating glass price label
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xB30E1422))
                                    .border(1.dp, FitmanColors.ElectricLime.copy(alpha = 0.45f), RoundedCornerShape(8.dp))
                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                            ) {
                                Text(
                                    text = game.displayPrice,
                                    color = FitmanColors.ElectricLime,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            if (game.tag.isNotBlank()) {
                                Spacer(Modifier.width(8.dp))
                                Text(
                                    text = "•  ${game.tag}",
                                    color = FitmanColors.TextSecondary,
                                    fontSize = 12.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }

                    LiquidGlassButton(
                        text = "EXPLORE",
                        onClick = { onExplore(game) },
                        isPrimary = true,
                        trailingIcon = Icons.Default.ArrowForward
                    )
                }
            }
        }
    }
}
