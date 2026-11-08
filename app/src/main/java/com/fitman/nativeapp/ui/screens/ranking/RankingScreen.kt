package com.fitman.nativeapp.ui.screens.ranking

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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.fitman.nativeapp.data.model.Game
import com.fitman.nativeapp.ui.components.CategoryUnderlineTabs
import com.fitman.nativeapp.ui.components.GameHubTopBar
import com.fitman.nativeapp.ui.theme.FitmanColors
import kotlinx.coroutines.launch

/**
 * GameHub Ranking Screen faithfully matched to Screenshots 2 and 3:
 * - Back button & search bar pill
 * - "Popular" and "Hot Games" tabs
 * - Swipeable side-by-side Top Chart ranking cards
 * - Gold/Silver/Bronze badges for ranks 1-3
 * - Star ratings (★ 8.3, ★ 10.0, ★ 9.6) & genres
 */
@Composable
fun RankingScreen(
    games: List<Game>,
    onGameClick: (Game) -> Unit,
    onBackClick: () -> Unit,
    onSearchClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val tabs = listOf("Popular", "Hot Games")
    val pagerState = rememberPagerState(pageCount = { tabs.size })
    val coroutineScope = rememberCoroutineScope()

    // Curated fallback titles if dynamic games list is shorter than 10
    val popularList = if (games.size >= 10) games.take(10) else defaultPopularGames()
    val hotGamesList = if (games.size >= 6) games.take(10) else defaultHotGames()

    val searchPlaceholder = if (pagerState.currentPage == 0) {
        "NARUTO SHIPPUDEN: Ultimate Ninja S..."
    } else {
        "FIFA 22"
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(FitmanColors.WarmBackgroundGradient)
    ) {
        // Top Bar with Back Arrow and Search Pill (Screenshots 2 & 3)
        GameHubTopBar(
            queryText = searchPlaceholder,
            onSearchClick = onSearchClick,
            onBackClick = onBackClick
        )

        // Subtabs: Popular & Hot Games
        CategoryUnderlineTabs(
            tabs = tabs,
            selectedTab = tabs[pagerState.currentPage],
            onTabSelected = { selected ->
                val index = tabs.indexOf(selected)
                if (index >= 0) {
                    coroutineScope.launch { pagerState.animateScrollToPage(index) }
                }
            }
        )

        Spacer(Modifier.height(8.dp))

        // Horizontal Pager with Card Peeking (matching Screenshot 3 where Hot Games card peeks on right)
        HorizontalPager(
            state = pagerState,
            contentPadding = PaddingValues(start = 16.dp, end = 24.dp),
            pageSpacing = 14.dp,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            if (page == 0) {
                // Popular Top 10 Chart (Screenshot 3)
                PopularRankCard(
                    games = popularList,
                    onGameClick = onGameClick
                )
            } else {
                // Hot Games Rated Chart (Screenshot 2)
                HotGamesRankCard(
                    games = hotGamesList,
                    onGameClick = onGameClick
                )
            }
        }
    }
}

/**
 * Popular 1-10 Chart Card with Gold/Silver/Bronze badges (Screenshot 3).
 */
@Composable
private fun PopularRankCard(
    games: List<Game>,
    onGameClick: (Game) -> Unit,
    modifier: Modifier = Modifier
) {
    val cardShape = RoundedCornerShape(24.dp)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(cardShape)
            .background(FitmanColors.WarmCardBackground)
            .border(1.dp, FitmanColors.WarmCardBorder, cardShape)
            .padding(vertical = 12.dp, horizontal = 14.dp)
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            itemsIndexed(games) { index, game ->
                val rank = index + 1
                PopularRankRow(
                    rank = rank,
                    game = game,
                    onClick = { onGameClick(game) }
                )
            }
        }
    }
}

@Composable
private fun PopularRankRow(
    rank: Int,
    game: Game,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Rank Indicator (Gold #1, Silver #2, Bronze #3, or Gray number)
        Box(
            modifier = Modifier.width(32.dp),
            contentAlignment = Alignment.Center
        ) {
            when (rank) {
                1 -> RankBadge(number = "1", backgroundColor = FitmanColors.GoldRank)
                2 -> RankBadge(number = "2", backgroundColor = FitmanColors.SilverRank)
                3 -> RankBadge(number = "3", backgroundColor = FitmanColors.BronzeRank)
                else -> Text(
                    text = "$rank",
                    color = FitmanColors.TextMutedWarm,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(Modifier.width(10.dp))

        // Cover artwork (squircle, 48.dp)
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(FitmanColors.WarmCardDark)
        ) {
            AsyncImage(
                model = game.image,
                contentDescription = game.title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(Modifier.width(14.dp))

        // Title
        Text(
            text = game.title,
            color = Color.White,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun RankBadge(
    number: String,
    backgroundColor: Color
) {
    Box(
        modifier = Modifier
            .size(24.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = number,
            color = Color.White,
            fontSize = 13.sp,
            fontWeight = FontWeight.ExtraBold
        )
    }
}

/**
 * Hot Games Chart with Star Ratings & Genre Subtitles (Screenshot 2).
 */
@Composable
private fun HotGamesRankCard(
    games: List<Game>,
    onGameClick: (Game) -> Unit,
    modifier: Modifier = Modifier
) {
    val cardShape = RoundedCornerShape(24.dp)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(cardShape)
            .background(FitmanColors.WarmCardBackground)
            .border(1.dp, FitmanColors.WarmCardBorder, cardShape)
            .padding(vertical = 12.dp, horizontal = 14.dp)
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            itemsIndexed(games) { _, game ->
                HotGameRow(
                    game = game,
                    onClick = { onGameClick(game) }
                )
            }
        }
    }
}

@Composable
private fun HotGameRow(
    game: Game,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Cover artwork (squircle, 52.dp)
        Box(
            modifier = Modifier
                .size(52.dp)
                .clip(RoundedCornerShape(13.dp))
                .background(FitmanColors.WarmCardDark)
        ) {
            AsyncImage(
                model = game.image,
                contentDescription = game.title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(Modifier.width(14.dp))

        // Info Column
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = game.title,
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(Modifier.height(3.dp))

            Text(
                text = game.genre,
                color = FitmanColors.TextMutedWarm,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(Modifier.width(8.dp))

        // Star Rating on Right (e.g. ★ 8.3, ★ 10.0, ★ 9.6)
        Text(
            text = "★ ${game.displayRating}",
            color = FitmanColors.StarGold,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

// Fallback items matching screenshot 3
private fun defaultPopularGames(): List<Game> = listOf(
    Game(271590, "Grand Theft Auto V Legacy", "https://shared.cloudflare.steamstatic.com/store_item_assets/steam/apps/271590/header.jpg"),
    Game(349040, "NARUTO SHIPPUDEN: Ultimate Ninja S...", "https://shared.cloudflare.steamstatic.com/store_item_assets/steam/apps/349040/header.jpg"),
    Game(1506830, "FIFA 22", "https://shared.cloudflare.steamstatic.com/store_item_assets/steam/apps/1506830/header.jpg"),
    Game(418370, "Resident Evil 7 Biohazard", "https://shared.cloudflare.steamstatic.com/store_item_assets/steam/apps/418370/header.jpg"),
    Game(1938090, "Call of Duty®: Modern Warfare® II", "https://shared.cloudflare.steamstatic.com/store_item_assets/steam/apps/1938090/header.jpg"),
    Game(1817070, "You.Tapes", "https://shared.cloudflare.steamstatic.com/store_item_assets/steam/apps/1817070/header.jpg"),
    Game(1817070, "Marvel’s Spider-Man Remastered", "https://shared.cloudflare.steamstatic.com/store_item_assets/steam/apps/1817070/header.jpg"),
    Game(227300, "Euro Truck Simulator 2 Demo", "https://shared.cloudflare.steamstatic.com/store_item_assets/steam/apps/227300/header.jpg"),
    Game(1672970, "Minecraft Dungeons II", "https://shared.cloudflare.steamstatic.com/store_item_assets/steam/apps/1672970/header.jpg"),
    Game(550, "Left 4 Dead 2", "https://shared.cloudflare.steamstatic.com/store_item_assets/steam/apps/550/header.jpg")
)

// Fallback items matching screenshot 2
private fun defaultHotGames(): List<Game> = listOf(
    Game(271590, "Grand Theft Auto V Legacy", "https://shared.cloudflare.steamstatic.com/store_item_assets/steam/apps/271590/header.jpg", rating = 8.3f, genre = "Action • Adventure"),
    Game(1091500, "Drilbert", "https://shared.cloudflare.steamstatic.com/store_item_assets/steam/apps/1091500/header.jpg", rating = 10.0f, genre = "Independent"),
    Game(292030, "The Witcher 3: Wild Hunt", "https://shared.cloudflare.steamstatic.com/store_item_assets/steam/apps/292030/header.jpg", rating = 9.6f, genre = "RPG"),
    Game(1174180, "Pigments", "https://shared.cloudflare.steamstatic.com/store_item_assets/steam/apps/1174180/header.jpg", rating = 10.0f, genre = "Action • Casual"),
    Game(1174180, "Red Dead Redemption 2", "https://shared.cloudflare.steamstatic.com/store_item_assets/steam/apps/1174180/header.jpg", rating = 9.1f, genre = "Action • Adventure"),
    Game(1245620, "Warbits+", "https://shared.cloudflare.steamstatic.com/store_item_assets/steam/apps/1245620/header.jpg", rating = 10.0f, genre = "Strategy • Independent")
)
