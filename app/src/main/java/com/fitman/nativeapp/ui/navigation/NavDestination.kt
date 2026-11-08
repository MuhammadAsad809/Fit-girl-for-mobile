package com.fitman.nativeapp.ui.navigation

sealed class NavDestination(val route: String, val title: String) {
    object Discover : NavDestination("discover", "Discover")
    object Search : NavDestination("search", "Search")
    object Library : NavDestination("library", "Library")
    object Settings : NavDestination("settings", "Settings")
}
