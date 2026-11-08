package com.fitman.nativeapp.ui.screens.library

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.BookmarkRemove
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.fitman.nativeapp.data.model.LibraryStatus
import com.fitman.nativeapp.ui.components.LiquidGlassButton
import com.fitman.nativeapp.ui.components.LiquidGlassCard
import com.fitman.nativeapp.ui.components.LiquidGlassChip
import com.fitman.nativeapp.ui.components.LiquidGlassIconButton
import com.fitman.nativeapp.ui.theme.FitmanColors

@Composable
fun LibraryScreen(
    favorites: List<Game>,
    onGameClick: (Game) -> Unit,
    onStatusChange: (Game, LibraryStatus) -> Unit,
    onRemoveFavorite: (Game) -> Unit,
    onExploreClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedFilter by remember { mutableStateOf<LibraryStatus?>(null) }

    val filteredGames = if (selectedFilter == null) {
        favorites
    } else {
        favorites.filter { it.libraryStatus == selectedFilter }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(FitmanColors.WarmBackgroundGradient)
            .padding(horizontal = 20.dp)
    ) {
        Spacer(Modifier.height(16.dp))

        // Screen Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "LIBRARY",
                        color = FitmanColors.TextPrimary,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 2.sp
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
                            text = "${favorites.size}",
                            color = FitmanColors.ElectricLime,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Text(
                    text = "Personal Catalogue & Playlists",
                    color = FitmanColors.TextSecondary,
                    fontSize = 12.sp
                )
            }

            Icon(
                imageVector = Icons.Default.Bookmarks,
                contentDescription = null,
                tint = FitmanColors.ElectricLime,
                modifier = Modifier.size(28.dp)
            )
        }

        Spacer(Modifier.height(14.dp))

        // Status Filter Chips
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(vertical = 4.dp)
        ) {
            item {
                LiquidGlassChip(
                    label = "All",
                    isSelected = selectedFilter == null,
                    onClick = { selectedFilter = null },
                    badgeText = "${favorites.size}"
                )
            }

            items(LibraryStatus.entries) { status ->
                val count = favorites.count { it.libraryStatus == status }
                LiquidGlassChip(
                    label = status.label,
                    isSelected = selectedFilter == status,
                    onClick = { selectedFilter = status },
                    badgeText = if (count > 0) "$count" else null
                )
            }
        }

        Spacer(Modifier.height(14.dp))

        if (filteredGames.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                LiquidGlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(26.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(28.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Bookmarks,
                            contentDescription = null,
                            tint = FitmanColors.ElectricLime.copy(alpha = 0.6f),
                            modifier = Modifier.size(48.dp)
                        )

                        Spacer(Modifier.height(14.dp))

                        Text(
                            text = if (selectedFilter == null) "Your Library Is Empty" else "No ${selectedFilter?.label} Games",
                            color = FitmanColors.TextPrimary,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(Modifier.height(6.dp))

                        Text(
                            text = if (selectedFilter == null) {
                                "Bookmark games from Discover or Search to curate your collection."
                            } else {
                                "Categorize your bookmarked games as '${selectedFilter?.label}' from the game details page."
                            },
                            color = FitmanColors.TextSecondary,
                            fontSize = 13.sp,
                            lineHeight = 18.sp
                        )

                        Spacer(Modifier.height(20.dp))

                        LiquidGlassButton(
                            text = "EXPLORE GAMES",
                            onClick = onExploreClick,
                            isPrimary = true,
                            leadingIcon = Icons.Default.Explore
                        )
                    }
                }
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(bottom = 100.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredGames, key = { it.id }) { game ->
                    CategorizedLibraryCard(
                        game = game,
                        onClick = { onGameClick(game) },
                        onStatusChange = { newStatus -> onStatusChange(game, newStatus) },
                        onRemove = { onRemoveFavorite(game) }
                    )
                }
            }
        }
    }
}

@Composable
private fun CategorizedLibraryCard(
    game: Game,
    onClick: () -> Unit,
    onStatusChange: (LibraryStatus) -> Unit,
    onRemove: () -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }

    LiquidGlassCard(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
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
                        // Price tag
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(FitmanColors.GlassSurfaceElevated)
                                .border(1.dp, FitmanColors.ElectricLime.copy(alpha = 0.35f), RoundedCornerShape(8.dp))
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "",
                                color = FitmanColors.ElectricLime,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(Modifier.width(8.dp))

                        // Category Pill Switcher
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0x33B7FF6A))
                                .clickable { showMenu = !showMenu }
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = game.libraryStatus.label,
                                color = FitmanColors.ElectricLime,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                LiquidGlassIconButton(
                    icon = Icons.Default.BookmarkRemove,
                    onClick = onRemove,
                    tint = FitmanColors.TextMuted,
                    size = 36.dp,
                    iconSize = 18.dp,
                    contentDescription = "Remove Bookmark"
                )
            }

            // Quick Category selector bar when tapped
            if (showMenu) {
                Spacer(Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    LibraryStatus.entries.forEach { status ->
                        val isCurrent = game.libraryStatus == status
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isCurrent) FitmanColors.ElectricLime else FitmanColors.GlassSurfaceElevated)
                                .clickable {
                                    onStatusChange(status)
                                    showMenu = false
                                }
                                .padding(vertical = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = status.label,
                                color = if (isCurrent) Color.Black else FitmanColors.TextSecondary,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}
