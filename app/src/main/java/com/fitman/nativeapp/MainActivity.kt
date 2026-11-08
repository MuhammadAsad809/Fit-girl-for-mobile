package com.fitman.nativeapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.fitman.nativeapp.data.model.Game
import com.fitman.nativeapp.data.model.GameDetail
import com.fitman.nativeapp.data.model.LibraryStatus
import com.fitman.nativeapp.data.remote.FitGirlApi
import com.fitman.nativeapp.data.remote.FitGirlDetail
import com.fitman.nativeapp.data.remote.SteamApi
import com.fitman.nativeapp.data.repository.FavoritesRepository
import com.fitman.nativeapp.data.repository.SettingsRepository
import com.fitman.nativeapp.ui.components.LiquidGlassLoadingIndicator
import com.fitman.nativeapp.ui.components.LiquidGlassNavigationBar
import com.fitman.nativeapp.ui.navigation.NavDestination
import com.fitman.nativeapp.ui.screens.details.GameDetailScreen
import com.fitman.nativeapp.ui.screens.discover.DiscoverScreen
import com.fitman.nativeapp.ui.screens.library.LibraryScreen
import com.fitman.nativeapp.ui.screens.search.SearchScreen
import com.fitman.nativeapp.ui.screens.settings.SettingsScreen
import com.fitman.nativeapp.ui.theme.FitmanColors
import com.fitman.nativeapp.ui.theme.FitmanTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FitmanTheme {
                FitmanApp(
                    openUrl = { url ->
                        runCatching {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                            startActivity(intent)
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun FitmanApp(openUrl: (String) -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val favoritesRepo = remember { FavoritesRepository(context) }
    val settingsRepo = remember { SettingsRepository(context) }

    var currentDestination by remember { mutableStateOf<NavDestination>(NavDestination.Discover) }
    var selectedCategory by remember { mutableStateOf("top_sellers") }
    // "steam" | "fitgirl"
    var catalogSource by remember { mutableStateOf("steam") }

    // Discover State
    var discoverGames by remember { mutableStateOf<List<Game>>(emptyList()) }
    var discoverLoading by remember { mutableStateOf(false) }
    var discoverError by remember { mutableStateOf("") }
    var discoverPage by remember { mutableIntStateOf(0) }

    // Search State
    var searchQuery by remember { mutableStateOf("") }
    var searchResults by remember { mutableStateOf<List<Game>>(emptyList()) }
    var searchLoading by remember { mutableStateOf(false) }
    var searchError by remember { mutableStateOf("") }
    var recentSearches by remember { mutableStateOf(settingsRepo.getRecentSearches()) }

    // Details State
    var selectedGame by remember { mutableStateOf<Game?>(null) }
    var gameDetail by remember { mutableStateOf<GameDetail?>(null) }
    var detailLoading by remember { mutableStateOf(false) }
    var repackLoading by remember { mutableStateOf(false) }
    var repackError by remember { mutableStateOf("") }
    var repackDetail by remember { mutableStateOf<FitGirlDetail?>(null) }

    // Favorites State
    var favoritesList by remember { mutableStateOf(favoritesRepo.getFavorites()) }

    // Handle back button
    BackHandler(enabled = selectedGame != null || currentDestination != NavDestination.Discover) {
        if (selectedGame != null) {
            selectedGame = null
            gameDetail = null
        } else if (currentDestination != NavDestination.Discover) {
            currentDestination = NavDestination.Discover
        }
    }

    // Function to load discover games
    fun loadDiscover(categoryKey: String) {
        selectedCategory = categoryKey
        discoverLoading = true
        discoverError = ""
        discoverPage = 0
        scope.launch {
            try {
                val list = withContext(Dispatchers.IO) {
                    SteamApi.featured(categoryKey, settingsRepo.storeRegion)
                }
                discoverGames = list
                if (list.isEmpty()) {
                    discoverError = "No games found in this category."
                }
            } catch (e: Exception) {
                discoverError = e.message ?: "Failed to load catalogue."
            } finally {
                discoverLoading = false
            }
        }
    }

    // Function to perform search
    fun performSearch(query: String) {
        val q = query.trim()
        if (q.isEmpty()) return
        searchLoading = true
        searchError = ""
        settingsRepo.addRecentSearch(q)
        recentSearches = settingsRepo.getRecentSearches()

        scope.launch {
            try {
                val list = withContext(Dispatchers.IO) {
                    SteamApi.search(q, settingsRepo.storeRegion)
                }
                searchResults = list
                if (list.isEmpty()) {
                    searchError = "No games matched \"$q\"."
                }
            } catch (e: Exception) {
                searchError = e.message ?: "Search request failed."
            } finally {
                searchLoading = false
            }
        }
    }

    // Function to open details
    fun openGameDetails(game: Game) {
        selectedGame = game
        gameDetail = null
        detailLoading = true
        repackDetail = null
        repackError = ""
        repackLoading = false
        scope.launch {
            try {
                val detail = withContext(Dispatchers.IO) {
                    SteamApi.details(game.id, settingsRepo.storeRegion)
                }
                gameDetail = detail
            } catch (e: Exception) {
                gameDetail = GameDetail(
                    id = game.id,
                    title = game.title,
                    image = game.image,
                    description = "View complete media and details on the official Steam Store.",
                    developer = "Steam Partner",
                    publisher = "Steam",
                    price = game.price,
                    url = game.url
                )
            } finally {
                detailLoading = false
            }
        }
    }

    fun loadRepackForCurrent() {
        val title = gameDetail?.title ?: selectedGame?.title ?: return
        repackLoading = true
        repackError = ""
        repackDetail = null
        scope.launch {
            try {
                val result = withContext(Dispatchers.IO) {
                    FitGirlApi.findRepackForTitle(title)
                }
                if (result == null) {
                    repackError = "No FitGirl repack found for this title."
                } else {
                    repackDetail = result
                }
            } catch (e: Exception) {
                repackError = e.message ?: "Failed to fetch repack links."
            } finally {
                repackLoading = false
            }
        }
    }

    // Initial load
    LaunchedEffect(Unit) {
        loadDiscover("top_sellers")
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = FitmanColors.MainBackground,
        bottomBar = {
            if (selectedGame == null) {
                LiquidGlassNavigationBar(
                    currentDestination = currentDestination,
                    onNavigate = { dest -> currentDestination = dest }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(FitmanColors.MainBackground)
        ) {
            if (selectedGame != null) {
                // Game Details View
                if (detailLoading) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        LiquidGlassLoadingIndicator(size = 56.dp)
                    }
                } else {
                    val detail = gameDetail
                    if (detail != null) {
                        val isFav = favoritesList.any { it.id == detail.id }
                        val currentStatus = favoritesList.firstOrNull { it.id == detail.id }?.libraryStatus
                        GameDetailScreen(
                            game = detail,
                            isFavorite = isFav,
                            currentStatus = currentStatus,
                            onToggleFavorite = {
                                val current = selectedGame ?: return@GameDetailScreen
                                favoritesRepo.toggleFavorite(current)
                                favoritesList = favoritesRepo.getFavorites()
                            },
                            onStatusChange = { newStatus ->
                                val current = selectedGame ?: return@GameDetailScreen
                                favoritesRepo.setGameStatus(current, newStatus)
                                favoritesList = favoritesRepo.getFavorites()
                            },
                            onBack = {
                                selectedGame = null
                                gameDetail = null
                            },
                            openUrl = openUrl,
                            onGetRepack = { loadRepackForCurrent() },
                            repackLoading = repackLoading,
                            repackError = repackError,
                            repack = repackDetail
                        )
                    }
                }
            } else {
                // Tab Content
                AnimatedContent(
                    targetState = currentDestination,
                    transitionSpec = {
                        (fadeIn() + slideInHorizontally { width -> width / 4 })
                            .togetherWith(fadeOut() + slideOutHorizontally { width -> -width / 4 })
                    },
                    label = "tabTransition"
                ) { destination ->
                    when (destination) {
                        NavDestination.Discover -> {
                            DiscoverScreen(
                                games = discoverGames,
                                selectedCategory = selectedCategory,
                                catalogSource = catalogSource,
                                onCatalogSourceChange = { src ->
                                    catalogSource = src
                                    loadDiscover(selectedCategory)
                                },
                                onCategorySelected = { key -> loadDiscover(key) },
                                onGameClick = { game -> openGameDetails(game) },
                                onRefresh = { loadDiscover(selectedCategory) },
                                onQuickSearchClick = { currentDestination = NavDestination.Search },
                                isLoading = discoverLoading,
                                errorMessage = discoverError,
                                currentPage = discoverPage,
                                onPageChange = { discoverPage = it }
                            )
                        }

                        NavDestination.Search -> {
                            SearchScreen(
                                query = searchQuery,
                                onQueryChange = { searchQuery = it },
                                onSearch = { q -> performSearch(q) },
                                catalogSource = catalogSource,
                                onCatalogSourceChange = { src ->
                                    catalogSource = src
                                    if (searchQuery.isNotBlank()) performSearch(searchQuery)
                                },
                                searchResults = searchResults,
                                recentSearches = recentSearches,
                                onRecentSearchClick = { term ->
                                    searchQuery = term
                                    performSearch(term)
                                },
                                onClearRecentSearches = {
                                    settingsRepo.clearRecentSearches()
                                    recentSearches = emptyList()
                                },
                                isLoading = searchLoading,
                                errorMessage = searchError,
                                onGameClick = { game -> openGameDetails(game) }
                            )
                        }

                        NavDestination.Library -> {
                            LibraryScreen(
                                favorites = favoritesList,
                                onGameClick = { game -> openGameDetails(game) },
                                onStatusChange = { game, newStatus ->
                                    favoritesRepo.setGameStatus(game, newStatus)
                                    favoritesList = favoritesRepo.getFavorites()
                                },
                                onRemoveFavorite = { game ->
                                    favoritesRepo.removeFavorite(game.id)
                                    favoritesList = favoritesRepo.getFavorites()
                                },
                                onExploreClick = { currentDestination = NavDestination.Discover }
                            )
                        }

                        NavDestination.Settings -> {
                            SettingsScreen(settingsRepository = settingsRepo)
                        }
                    }
                }
            }
        }
    }
}
