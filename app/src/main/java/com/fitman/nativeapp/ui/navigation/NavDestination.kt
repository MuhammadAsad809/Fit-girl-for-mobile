package com.fitman.nativeapp.ui.navigation

sealed class NavDestination(val route: String, val title: String) {
    object Home : NavDestination("home", "Home")
    object Community : NavDestination("community", "Community")
    object Ranking : NavDestination("ranking", "Ranking")
    object Library : NavDestination("library", "Library")
    object Profile : NavDestination("profile", "Profile")

    // Sub-destination
    object Search : NavDestination("search", "Search")

    companion object {
        val Discover = Home
        val Settings = Profile
    }
}
