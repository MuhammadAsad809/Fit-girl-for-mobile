package com.fitman.nativeapp.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.fitman.nativeapp.ui.components.CategoryUnderlineTabs
import com.fitman.nativeapp.ui.components.GameHubHeroBanner
import com.fitman.nativeapp.ui.components.GameHubTopBar
import com.fitman.nativeapp.ui.components.LiquidGlassShimmerList
import com.fitman.nativeapp.ui.theme.FitmanColors

/**
 * GameHub Home Screen faithfully matched to Screenshot 1:
 * - Search bar with Notification & Scanner icons
 * - Underline subtabs: Recommended, PC Games, Retro Games
 * - Featured "Today" cinematic hero banner with quote & mute button
 * - "Your Next Favorite" horizontal squircle game cards
 * - "News & Updates" horizontal banner carousel
 */
@Composable
fun HomeScreen(
    games: List<Game>,
    onGameClick: (Game) -> Unit,
    onSearchClick: () -> Unit,
    onNotificationClick: () -> Unit,
    onScanClick: () -> Unit,
    isLoading: Boolean,
    modifier: Modifier = Modifier
) {
    var selectedCategoryTab by remember { mutableStateOf("Recommended") }
    val categoryTabs = listOf("Recommended", "PC Games", "Retro Games")

    val heroGame = games.firstOrNull() ?: Game(
        id = 271590,
        title = "Grand Theft Auto V Legacy",
        image = "https://shared.cloudflare.steamstatic.com/store_item_assets/steam/apps/271590/header.jpg",
        genre = "Action • Adventure",
        quote = "Play Grand Theft Auto V PC in Ultra-Realistic..."
    )

    val nextFavoriteGames = games.drop(1).ifEmpty {
        listOf(
            Game(271590, "Grand Theft Auto V", "https://shared.cloudflare.steamstatic.com/store_item_assets/steam/apps/271590/header.jpg"),
            Game(1091500, "Cyberpunk 2077", "https://shared.cloudflare.steamstatic.com/store_item_assets/steam/apps/1091500/header.jpg"),
            Game(292030, "The Witcher 3: Wild Hunt", "https://shared.cloudflare.steamstatic.com/store_item_assets/steam/apps/292030/header.jpg"),
            Game(1174180, "Red Dead Redemption 2", "https://shared.cloudflare.steamstatic.com/store_item_assets/steam/apps/1174180/header.jpg"),
            Game(1245620, "Elden Ring", "https://shared.cloudflare.steamstatic.com/store_item_assets/steam/apps/1245620/header.jpg")
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(FitmanColors.WarmBackgroundGradient)
    ) {
        // Top Search Bar with Bell and Scanner icons (Screenshot 1)
        GameHubTopBar(
            queryText = "FIFA 22",
            onSearchClick = onSearchClick,
            onNotificationClick = onNotificationClick,
            onScanClick = onScanClick
        )

        // Subtabs: Recommended, PC Games, Retro Games (Screenshot 1)
        CategoryUnderlineTabs(
            tabs = categoryTabs,
            selectedTab = selectedCategoryTab,
            onTabSelected = { selectedCategoryTab = it }
        )

        Spacer(Modifier.height(8.dp))

        if (isLoading && games.isEmpty()) {
            LiquidGlassShimmerList()
        } else {
            LazyColumn(
                contentPadding = PaddingValues(bottom = 90.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Featured Hero Banner
                item {
                    Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                        GameHubHeroBanner(
                            game = heroGame,
                            onClick = onGameClick
                        )
                    }
                }

                // "Your Next Favorite" Section
                item {
                    Column {
                        Text(
                            text = "Your Next Favorite",
                            color = Color.White,
                            fontSize = 19.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                        )

                        Spacer(Modifier.height(8.dp))

                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            items(nextFavoriteGames) { game ->
                                NextFavoriteCard(
                                    game = game,
                                    onClick = { onGameClick(game) }
                                )
                            }
                        }
                    }
                }

                // "News & Updates" Section
                item {
                    Column {
                        Text(
                            text = "News & Updates",
                            color = Color.White,
                            fontSize = 19.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                        )

                        Spacer(Modifier.height(8.dp))

                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            item {
                                NewsBannerCard(
                                    title = "GameHub GSFG 1.1",
                                    subtitle = "All-New AI Frame Interpolation Upgrade",
                                    gradient = Brush.horizontalGradient(
                                        listOf(Color(0xFF0D253A), Color(0xFF0F3E50))
                                    )
                                )
                            }
                            item {
                                NewsBannerCard(
                                    title = "FitGirl Direct Repacks",
                                    subtitle = "Automated Clean Full Game Torrent Downloader",
                                    gradient = Brush.horizontalGradient(
                                        listOf(Color(0xFF2E2218), Color(0xFF45301F))
                                    )
                                )
                            }
                            item {
                                NewsBannerCard(
                                    title = "Steam Deck OLED Boost",
                                    subtitle = "Optimal 60 FPS Settings Configured",
                                    gradient = Brush.horizontalGradient(
                                        listOf(Color(0xFF1E1E2E), Color(0xFF2A203C))
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Squircle game card for "Your Next Favorite" section (Screenshot 1).
 */
@Composable
private fun NextFavoriteCard(
    game: Game,
    onClick: () -> Unit
) {
    val cardShape = RoundedCornerShape(18.dp)

    Column(
        modifier = Modifier
            .width(108.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
    ) {
        // Square squircle artwork
        Box(
            modifier = Modifier
                .size(108.dp)
                .clip(cardShape)
                .background(FitmanColors.WarmCardBackground)
                .border(1.dp, FitmanColors.WarmCardBorder, cardShape)
        ) {
            AsyncImage(
                model = game.image,
                contentDescription = game.title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(Modifier.height(8.dp))

        // Game Title
        Text(
            text = game.title,
            color = Color.White,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

/**
 * Banner card for "News & Updates" section (Screenshot 1).
 */
@Composable
private fun NewsBannerCard(
    title: String,
    subtitle: String,
    gradient: Brush
) {
    val cardShape = RoundedCornerShape(18.dp)

    Box(
        modifier = Modifier
            .width(250.dp)
            .height(96.dp)
            .clip(cardShape)
            .background(gradient)
            .border(1.dp, Color(0x33FFFFFF), cardShape)
            .padding(14.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Column {
            Text(
                text = title,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.ExtraBold
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = subtitle,
                color = Color(0xFFD4D0CB),
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
