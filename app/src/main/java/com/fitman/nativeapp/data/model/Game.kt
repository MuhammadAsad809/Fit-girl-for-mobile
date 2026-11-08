package com.fitman.nativeapp.data.model

data class Game(
    val id: Int,
    val title: String,
    val image: String,
    val price: String = "",
    val url: String = "https://store.steampowered.com/app/$id/",
    val tag: String = "",
    val discountPercent: Int = 0
) {
    val displayPrice: String
        get() = when {
            price.isBlank() -> "Check Steam"
            price.equals("Free", ignoreCase = true) -> "Free to Play"
            else -> price
        }
}

data class GameDetail(
    val id: Int = 0,
    val title: String,
    val image: String,
    val description: String,
    val developer: String,
    val publisher: String,
    val price: String,
    val url: String,
    val releaseDate: String = "",
    val reviews: String = "",
    val genres: List<String> = emptyList()
)
