package com.fitman.nativeapp.ui.screens.search

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.ManageSearch
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fitman.nativeapp.data.model.Game
import com.fitman.nativeapp.ui.components.LiquidGlassButton
import com.fitman.nativeapp.ui.components.LiquidGlassCard
import com.fitman.nativeapp.ui.components.LiquidGlassLoadingIndicator
import com.fitman.nativeapp.ui.components.LiquidGlassChip
import com.fitman.nativeapp.ui.components.LiquidGlassSearchBar
import com.fitman.nativeapp.ui.screens.discover.GameItemCard
import com.fitman.nativeapp.ui.theme.FitmanColors

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SearchScreen(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    catalogSource: String = "steam",
    onCatalogSourceChange: (String) -> Unit = {},
    searchResults: List<Game>,
    recentSearches: List<String>,
    onRecentSearchClick: (String) -> Unit,
    onClearRecentSearches: () -> Unit,
    isLoading: Boolean,
    errorMessage: String,
    onGameClick: (Game) -> Unit,
    modifier: Modifier = Modifier
) {
    val trendingSuggestions = listOf(
        "Cyberpunk 2077",
        "Elden Ring",
        "Hades II",
        "Baldur's Gate 3",
        "Witcher 3",
        "Grand Theft Auto"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(FitmanColors.WarmBackgroundGradient)
            .padding(horizontal = 20.dp)
    ) {
        Spacer(Modifier.height(16.dp))

        // Screen Title
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "SEARCH",
                    color = FitmanColors.TextPrimary,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 2.sp
                )
                Text(
                    text = "Steam Global Catalogue Query",
                    color = FitmanColors.TextSecondary,
                    fontSize = 12.sp
                )
            }
            Icon(
                imageVector = Icons.Default.ManageSearch,
                contentDescription = null,
                tint = FitmanColors.ElectricLime,
                modifier = Modifier.size(28.dp)
            )
        }

        Spacer(Modifier.height(16.dp))

        // Floating Search Bar
        LiquidGlassSearchBar(
            query = query,
            onQueryChange = onQueryChange,
            onSearch = onSearch,
            placeholderText = "Enter title, franchise, or genre..."
        )

        Spacer(Modifier.height(10.dp))
Spacer(Modifier.height(14.dp))

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
                                text = "QUERYING STEAM NODES...",
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
                        LiquidGlassCard(modifier = Modifier.fillMaxWidth()) {
                            Column(
                                modifier = Modifier.padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "SEARCH TRANSMISSION FAILED",
                                    color = FitmanColors.ElectricLime,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp
                                )
                                Spacer(Modifier.height(8.dp))
                                Text(
                                    text = errorMessage,
                                    color = FitmanColors.TextSecondary,
                                    fontSize = 13.sp
                                )
                                Spacer(Modifier.height(14.dp))
                                LiquidGlassButton(
                                    text = "RETRY SEARCH",
                                    onClick = { onSearch(query) },
                                    isPrimary = true
                                )
                            }
                        }
                    }
                }

                searchResults.isNotEmpty() -> {
                    LazyColumn(
                        contentPadding = PaddingValues(bottom = 100.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        item {
                            Text(
                                text = "RESULTS (${searchResults.size})",
                                color = FitmanColors.TextMuted,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.5.sp
                            )
                        }
                        items(searchResults) { game ->
                            GameItemCard(
                                game = game,
                                onClick = { onGameClick(game) }
                            )
                        }
                    }
                }

                else -> {
                    // Empty state / Search Explorer
                    LazyColumn(
                        contentPadding = PaddingValues(bottom = 100.dp),
                        verticalArrangement = Arrangement.spacedBy(18.dp)
                    ) {
                        // Recent Searches
                        if (recentSearches.isNotEmpty()) {
                            item {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.History,
                                            contentDescription = null,
                                            tint = FitmanColors.ElectricLime,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(Modifier.width(6.dp))
                                        Text(
                                            text = "RECENT SEARCHES",
                                            color = FitmanColors.TextMuted,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            letterSpacing = 1.5.sp
                                        )
                                    }

                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .clickable { onClearRecentSearches() }
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = "Clear",
                                            color = FitmanColors.TextSecondary,
                                            fontSize = 11.sp
                                        )
                                    }
                                }

                                Spacer(Modifier.height(8.dp))

                                FlowRow(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    recentSearches.forEach { term ->
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(12.dp))
                                                .background(FitmanColors.GlassSurfaceElevated)
                                                .border(1.dp, FitmanColors.GlassBorderSubtle, RoundedCornerShape(12.dp))
                                                .clickable { onRecentSearchClick(term) }
                                                .padding(horizontal = 12.dp, vertical = 8.dp)
                                        ) {
                                            Text(
                                                text = term,
                                                color = FitmanColors.TextPrimary,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Medium
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        // Trending suggestions
                        item {
                            Text(
                                text = "TRENDING TITLES",
                                color = FitmanColors.TextMuted,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.5.sp
                            )

                            Spacer(Modifier.height(8.dp))

                            FlowRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                trendingSuggestions.forEach { term ->
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(FitmanColors.GlassSurface)
                                            .border(1.dp, FitmanColors.GlassBorder, RoundedCornerShape(12.dp))
                                            .clickable {
                                                onQueryChange(term)
                                                onSearch(term)
                                            }
                                            .padding(horizontal = 12.dp, vertical = 8.dp)
                                    ) {
                                        Text(
                                            text = term,
                                            color = FitmanColors.ElectricLime,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }
                                }
                            }
                        }

                        // Futuristic Obsidian Empty State Card
                        item {
                            LiquidGlassCard(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(24.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(24.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Search,
                                        contentDescription = null,
                                        tint = FitmanColors.ElectricLime.copy(alpha = 0.7f),
                                        modifier = Modifier.size(40.dp)
                                    )
                                    Spacer(Modifier.height(12.dp))
                                    Text(
                                        text = "Explore 50,000+ Games",
                                        color = FitmanColors.TextPrimary,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(Modifier.height(6.dp))
                                    Text(
                                        text = "Search directly across the public Steam Store catalogue with real-time pricing and official metadata.",
                                        color = FitmanColors.TextSecondary,
                                        fontSize = 12.sp,
                                        lineHeight = 18.sp,
                                        modifier = Modifier.padding(horizontal = 8.dp)
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
