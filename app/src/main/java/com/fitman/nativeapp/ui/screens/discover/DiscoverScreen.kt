package com.fitman.nativeapp.ui.screens.discover

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.NavigateBefore
import androidx.compose.material.icons.filled.NavigateNext
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.fitman.nativeapp.ui.components.LiquidGlassChip
import com.fitman.nativeapp.ui.components.LiquidGlassIconButton
import com.fitman.nativeapp.ui.components.LiquidGlassLoadingIndicator
import com.fitman.nativeapp.ui.theme.FitmanColors

@Composable
fun DiscoverScreen(
    games: List<Game>,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit,
    onGameClick: (Game) -> Unit,
    onRefresh: () -> Unit,
    onQuickSearchClick: () -> Unit,
    isLoading: Boolean,
    errorMessage: String,
    currentPage: Int,
    onPageChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val categories = listOf(
        "Discover" to "top_sellers",
        "Popular" to "popular",
        "New Releases" to "new",
        "Specials" to "specials"
    )

    val pageSize = 10
    val totalPages = ((games.size - 1).coerceAtLeast(0) / pageSize) + 1
    val shownGames = games.drop(currentPage * pageSize).take(pageSize)
    val heroGame = games.firstOrNull()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(FitmanColors.MainBackground)
    ) {
        // Futuristic Obsidian Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "FITMAN",
                        color = FitmanColors.TextPrimary,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 3.sp
                    )
                    Spacer(Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(FitmanColors.ElectricLimeMuted)
                            .border(1.dp, FitmanColors.ElectricLime.copy(alpha = 0.5f), RoundedCornerShape(6.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "v2.0",
                            color = FitmanColors.ElectricLime,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Text(
                    text = "Liquid Glass Discovery",
                    color = FitmanColors.TextSecondary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                LiquidGlassIconButton(
                    icon = Icons.Default.Search,
                    onClick = onQuickSearchClick,
                    contentDescription = "Search"
                )
                LiquidGlassIconButton(
                    icon = Icons.Default.Refresh,
                    onClick = onRefresh,
                    tint = FitmanColors.ElectricLime,
                    contentDescription = "Refresh"
                )
            }
        }

        // Horizontal Category Chips
        LazyRow(
            contentPadding = PaddingValues(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.padding(bottom = 12.dp)
        ) {
            items(categories) { (label, key) ->
                LiquidGlassChip(
                    label = label,
                    isSelected = selectedCategory.equals(key, ignoreCase = true) ||
                            (key == "top_sellers" && selectedCategory.equals("Discover", ignoreCase = true)),
                    onClick = { onCategorySelected(key) }
                )
            }
        }

        // Main Content Area
        Box(modifier = Modifier.fillMaxSize()) {
            when {
                isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            LiquidGlassLoadingIndicator(size = 56.dp)
                            Spacer(Modifier.height(16.dp))
                            Text(
                                text = "STREAMING STEAM DATA...",
                                color = FitmanColors.ElectricLime,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 2.sp
                            )
                        }
                    }
                }

                errorMessage.isNotBlank() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        LiquidGlassCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "TRANSMISSION ERROR",
                                    color = Color(0xFFFF6B6B),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp
                                )
                                Spacer(Modifier.height(8.dp))
                                Text(
                                    text = errorMessage,
                                    color = FitmanColors.TextSecondary,
                                    fontSize = 13.sp
                                )
                                Spacer(Modifier.height(16.dp))
                                LiquidGlassButton(
                                    text = "RETRY",
                                    onClick = onRefresh,
                                    isPrimary = true
                                )
                            }
                        }
                    }
                }

                games.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No games found in this category.",
                            color = FitmanColors.TextSecondary,
                            fontSize = 14.sp
                        )
                    }
                }

                else -> {
                    LazyColumn(
                        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, bottom = 100.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        // Hero Spotlight Section (Only on page 0 and if heroGame exists)
                        if (currentPage == 0 && heroGame != null) {
                            item {
                                HeroSection(
                                    game = heroGame,
                                    onExplore = onGameClick
                                )
                                Spacer(Modifier.height(6.dp))
                            }
                        }

                        // Section Title
                        item {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = when (selectedCategory.lowercase()) {
                                        "popular" -> "TOP SELLERS"
                                        "new" -> "NEW RELEASES"
                                        "specials" -> "SPECIAL PROMOTIONS"
                                        else -> "FEATURED TITLES"
                                    },
                                    color = FitmanColors.TextMuted,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 2.sp
                                )
                                Text(
                                    text = "${games.size} Available",
                                    color = FitmanColors.TextSecondary,
                                    fontSize = 11.sp
                                )
                            }
                        }

                        // Game List Items
                        items(shownGames) { game ->
                            GameItemCard(
                                game = game,
                                onClick = { onGameClick(game) }
                            )
                        }

                        // Liquid Glass Pagination Controls
                        if (totalPages > 1) {
                            item {
                                Spacer(Modifier.height(8.dp))
                                LiquidGlassCard(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(20.dp)
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 14.dp, vertical = 8.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        LiquidGlassButton(
                                            text = "PREV",
                                            onClick = { onPageChange(currentPage - 1) },
                                            enabled = currentPage > 0,
                                            isPrimary = false,
                                            leadingIcon = Icons.Default.NavigateBefore
                                        )

                                        Text(
                                            text = "PAGE ${currentPage + 1} OF $totalPages",
                                            color = FitmanColors.TextSecondary,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            letterSpacing = 1.sp
                                        )

                                        LiquidGlassButton(
                                            text = "NEXT",
                                            onClick = { onPageChange(currentPage + 1) },
                                            enabled = currentPage < totalPages - 1,
                                            isPrimary = false,
                                            trailingIcon = Icons.Default.NavigateNext
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun GameItemCard(
    game: Game,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cardShape = RoundedCornerShape(20.dp)

    LiquidGlassCard(
        modifier = modifier.fillMaxWidth(),
        shape = cardShape,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Artwork
            AsyncImage(
                model = game.image,
                contentDescription = game.title,
                modifier = Modifier
                    .width(116.dp)
                    .height(72.dp)
                    .clip(RoundedCornerShape(14.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(Modifier.width(14.dp))

            // Information
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = game.title,
                    color = FitmanColors.TextPrimary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(Modifier.height(6.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Floating Price Tag
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(FitmanColors.GlassSurfaceElevated)
                            .border(1.dp, FitmanColors.ElectricLime.copy(alpha = 0.35f), RoundedCornerShape(8.dp))
                            .padding(horizontal = 8.dp, vertical = 2.dp)
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
                            text = game.tag,
                            color = FitmanColors.TextMuted,
                            fontSize = 11.sp
                        )
                    }
                }
            }

            // Right Chevron with subtle glow
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = FitmanColors.TextMuted,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
