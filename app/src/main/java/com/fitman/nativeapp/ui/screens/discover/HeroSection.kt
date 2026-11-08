package com.fitman.nativeapp.ui.screens.discover

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
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

@Composable
fun HeroSection(
    game: Game,
    onExplore: (Game) -> Unit,
    modifier: Modifier = Modifier
) {
    val heroShape = RoundedCornerShape(26.dp)

    LiquidGlassCard(
        modifier = modifier
            .fillMaxWidth()
            .height(230.dp),
        shape = heroShape,
        elevation = 14.dp,
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

            // Deep obsidian gradient overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color(0x66050608),
                                Color(0xE6050608),
                                Color(0xFF050608)
                            )
                        )
                    )
            )

            // Top-left Spotlight Badge
            Box(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(14.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0x99000000))
                    .border(1.dp, FitmanColors.GlassBorderHighlight, RoundedCornerShape(12.dp))
                    .padding(horizontal = 10.dp, vertical = 5.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.ElectricBolt,
                        contentDescription = null,
                        tint = FitmanColors.ElectricLime,
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(Modifier.width(5.dp))
                    Text(
                        text = "FEATURED SPOTLIGHT",
                        color = FitmanColors.ElectricLime,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }
            }

            // Bottom Content
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
                    Column(modifier = Modifier.weight(1f).padding(end = 12.dp)) {
                        Text(
                            text = game.title,
                            color = FitmanColors.TextPrimary,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            // Floating glass price label
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0x80131926))
                                    .border(1.dp, Color(0x33B7FF6A), RoundedCornerShape(8.dp))
                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                            ) {
                                Text(
                                    text = "DOWNLOAD",
                                    color = FitmanColors.ElectricLime,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            if (game.tag.isNotBlank()) {
                                Spacer(Modifier.width(8.dp))
                                Text(
                                    text = "•  ${game.tag}",
                                    color = FitmanColors.TextSecondary,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }

                    LiquidGlassButton(
                        text = "EXPLORE",
                        onClick = { onExplore(game) },
                        isPrimary = true,
                        leadingIcon = Icons.Default.Visibility
                    )
                }
            }
        }
    }
}
