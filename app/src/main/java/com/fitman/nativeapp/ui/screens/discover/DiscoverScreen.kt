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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.NavigateBefore
import androidx.compose.material.icons.filled.NavigateNext
import androidx.compose.material.icons.filled.NewReleases
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.TrendingUp
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
import com.fitman.nativeapp.ui.components.LiquidGlassIconButton
import com.fitman.nativeapp.ui.components.LiquidGlassSegmentedBar
import com.fitman.nativeapp.ui.components.LiquidGlassShimmerList
import com.fitman.nativeapp.ui.components.SegmentItem
import com.fitman.nativeapp.ui.theme.FitmanColors

/**
 * Apple iOS 45-inspired Liquid Glass Discover Screen.
 * Complete obsidian glassmorphism experience with unified segmented capsule,
 * shimmer skeletal transitions, and tactile spring feedback.
 */
@Composable
fun DiscoverScreen(
    games: List<Game>,
    selectedCategory: String,
    catalogSource: String = "steam",
    onCatalogSourceChange: (String) -> Unit = {},
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
    val categoryItems = listOf(
        SegmentItem("top_sellers", "Discover", Icons.Default.Explore),
        SegmentItem("popular", "Popular", Icons.Default.TrendingUp),
        SegmentItem("new", "New Releases", Icons.Default.NewReleases),
        SegmentItem("specials", "Specials", Icons.Default.LocalOffer)
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
        // Apple-style Futuristic Obsidian Header
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
                        letterSpacing = 3.5.sp
                    )
                    Spacer(Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(FitmanColors.ElectricLimeMuted)
                            .border(1.dp, FitmanColors.ElectricLime.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                            .padding(horizontal = 7.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "v2.2",
                            color = FitmanColors.ElectricLime,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 0.5.sp
                        )
                    }
                }
                Spacer(Modifier.height(2.dp))
                Text(
                    text = "Liquid Glass Discovery",
                    color = FitmanColors.TextSecondary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 0.3.sp
                )
            }

            // Quick Actions: Glass Icon Buttons
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                LiquidGlassIconButton(
                    icon = Icons.Default.Search,
                    onClick = onQuickSearchClick,
                    contentDescription = "Search Games"
                )
                LiquidGlassIconButton(
                    icon = Icons.Default.Refresh,
                    onClick = onRefresh,
                    tint = FitmanColors.ElectricLime,
                    contentDescription = "Refresh Catalogue"
                )
            }
        }

        // Apple-style Unified Frosted Glass Segmented Capsule Bar
        LiquidGlassSegmentedBar(
            items = categoryItems,
            selectedKey = selectedCategory,
            onItemSelected = onCategorySelected,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // Main Content Area
        Box(modifier = Modifier.fillMaxSize()) {
            when {
                isLoading -> {
                    // Apple iOS 45 Shimmer Skeletal Loading List
                    LiquidGlassShimmerList()
                }

                errorMessage.isNotBlank() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        LiquidGlassCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            shape = RoundedCornerShape(24.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(26.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "TRANSMISSION ERROR",
                                    color = Color(0xFFFF6B6B),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.2.sp
                                )
                                Spacer(Modifier.height(8.dp))
                                Text(
                                    text = errorMessage,
                                    color = FitmanColors.TextSecondary,
                                    fontSize = 13.sp,
                                    lineHeight = 18.sp
                                )
                                Spacer(Modifier.height(18.dp))
                                LiquidGlassButton(
                                    text = "RETRY TRANSMISSION",
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
                        LiquidGlassCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            shape = RoundedCornerShape(22.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "NO TITLES DISCOVERED",
                                    color = FitmanColors.TextPrimary,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp
                                )
                                Spacer(Modifier.height(6.dp))
                                Text(
                                    text = "No games found in this category at the moment.",
                                    color = FitmanColors.TextSecondary,
                                    fontSize = 12.sp
                                )
                            }
                        }
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

                        // Section Header
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
                                    fontWeight = FontWeight.ExtraBold,
                                    letterSpacing = 2.sp
                                )

                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(FitmanColors.GlassSurfaceElevated)
                                        .border(1.dp, FitmanColors.SpecularBorderBrush, RoundedCornerShape(10.dp))
                                        .padding(horizontal = 8.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "${games.size} AVAILABLE",
                                        color = FitmanColors.TextSecondary,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 0.5.sp
                                    )
                                }
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
                                Spacer(Modifier.height(6.dp))
                                LiquidGlassCard(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(24.dp)
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

/**
 * Apple iOS 45-inspired Liquid Glass Game Card.
 * Crisp artwork with continuous squircle rounding, floating pricing pills,
 * discount badges, and tactile spring scale feedback.
 */
@Composable
fun GameItemCard(
    game: Game,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cardShape = RoundedCornerShape(22.dp)

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
            // Artwork Container with Squircle Clipping & Subtle Hairline Border
            Box(
                modifier = Modifier
                    .width(116.dp)
                    .height(72.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .border(1.dp, FitmanColors.GlassBorderSubtle, RoundedCornerShape(14.dp))
            ) {
                AsyncImage(
                    model = game.image,
                    contentDescription = game.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(Modifier.width(14.dp))

            // Information Column
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

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // Floating Glass Price Tag
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

                    // Optional Discount Pill
                    if (game.discountPercent > 0) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(FitmanColors.ElectricLimeMuted)
                                .border(1.dp, FitmanColors.ElectricLime.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "-${game.discountPercent}%",
                                color = FitmanColors.ElectricLime,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    }

                    // Category Tag
                    if (game.tag.isNotBlank()) {
                        Text(
                            text = game.tag,
                            color = FitmanColors.TextMuted,
                            fontSize = 11.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }

            Spacer(Modifier.width(6.dp))

            // Right Frosted Glass Action Button
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(FitmanColors.GlassSurfaceElevated)
                    .border(1.dp, FitmanColors.SpecularBorderBrush, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = FitmanColors.TextSecondary,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}
