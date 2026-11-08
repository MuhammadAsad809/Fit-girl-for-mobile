package com.fitman.nativeapp.data.model

enum class LibraryStatus(val label: String) {
    FAVORITE("Favorites"),
    WISHLIST("Wishlist"),
    PLAYING("Playing"),
    COMPLETED("Completed");

    companion object {
        fun fromString(str: String?): LibraryStatus =
            entries.firstOrNull { it.name.equals(str, ignoreCase = true) } ?: FAVORITE
    }
}

data class Game(
    val id: Int,
    val title: String,
    val image: String,
    val price: String = "",
    val url: String = "https://store.steampowered.com/app/$id/",
    val tag: String = "",
    val discountPercent: Int = 0,
    val libraryStatus: LibraryStatus = LibraryStatus.FAVORITE
) {
    val displayPrice: String
        get() = when {
            price.isBlank() -> "Check Steam"
            price.equals("Free", ignoreCase = true) -> "Free to Play"
            else -> price
        }
}

data class Screenshot(
    val id: Int,
    val thumbnail: String,
    val full: String
)

data class PlatformSupport(
    val windows: Boolean = false,
    val mac: Boolean = false,
    val linux: Boolean = false
)

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
    val genres: List<String> = emptyList(),
    val metacriticScore: Int? = null,
    val metacriticUrl: String? = null,
    val screenshots: List<Screenshot> = emptyList(),
    val platforms: PlatformSupport = PlatformSupport(),
    val pcRequirementsMin: String = "",
    val pcRequirementsRec: String = "",
    val controllerSupport: String = "",
    val recommendationsCount: Int = 0,
    val backgroundImage: String = ""
)
