package com.fitman.nativeapp.data.remote

import com.fitman.nativeapp.data.model.Game
import com.fitman.nativeapp.data.model.GameDetail
import org.json.JSONObject
import org.jsoup.Jsoup
import java.net.URLEncoder

object SteamApi {
    private const val BASE = "https://store.steampowered.com"

    private fun doc(url: String): String {
        return Jsoup.connect(url)
            .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
            .timeout(15000)
            .ignoreContentType(true)
            .execute()
            .body()
    }

    private fun bestHeaderImage(id: Int, fallback: String): String {
        return if (id > 0) {
            "https://shared.cloudflare.steamstatic.com/store_item_assets/steam/apps/$id/header.jpg"
        } else {
            fallback
        }
    }

    fun search(q: String, region: String = "us"): List<Game> {
        val trimmed = q.trim()
        if (trimmed.isEmpty()) return emptyList()
        val encoded = URLEncoder.encode(trimmed, "UTF-8")
        val raw = doc("$BASE/api/storesearch/?term=$encoded&l=english&cc=$region")
        val json = JSONObject(raw)
        val arr = json.optJSONArray("items") ?: return emptyList()

        val list = mutableListOf<Game>()
        for (i in 0 until arr.length()) {
            val o = arr.optJSONObject(i) ?: continue
            val id = o.optInt("id")
            if (id <= 0) continue

            val name = o.optString("name", "Unknown Game")
            val tiny = o.optString("tiny_image", "")
            val image = bestHeaderImage(id, tiny)

            val priceObj = o.optJSONObject("price")
            val priceStr = if (priceObj != null) {
                val initial = priceObj.optInt("initial", 0)
                val finalPrice = priceObj.optInt("final", 0)
                if (finalPrice == 0 && initial == 0) {
                    "Free"
                } else {
                    val dollars = finalPrice / 100.0
                    String.format("$%.2f", dollars)
                }
            } else {
                o.optString("price", "")
            }

            list.add(
                Game(
                    id = id,
                    title = name,
                    image = image,
                    price = priceStr,
                    tag = "Search Match"
                )
            )
        }
        return list
    }

    fun featured(category: String, region: String = "us"): List<Game> {
        val raw = doc("$BASE/api/featuredcategories/?cc=$region&l=english")
        val json = JSONObject(raw)
        val sectionKey = when (category.lowercase()) {
            "popular", "top_sellers" -> "top_sellers"
            "new", "new_releases" -> "new_releases"
            "specials" -> "specials"
            else -> "top_sellers"
        }

        val section = json.optJSONObject(sectionKey) ?: return emptyList()
        val arr = section.optJSONArray("items") ?: return emptyList()

        val list = mutableListOf<Game>()
        for (i in 0 until arr.length()) {
            val o = arr.optJSONObject(i) ?: continue
            val id = o.optInt("id")
            if (id <= 0) continue

            val name = o.optString("name", "Untitled")
            val rawImage = o.optString("header_image", "")
            val image = if (rawImage.isNotBlank()) rawImage else bestHeaderImage(id, "")

            val discount = o.optInt("discount_percent", 0)
            val finalPriceCents = o.optInt("final_price", 0)
            val origPriceCents = o.optInt("original_price", 0)

            val priceStr = when {
                finalPriceCents == 0 && origPriceCents == 0 -> "Free to Play"
                finalPriceCents > 0 -> String.format("$%.2f", finalPriceCents / 100.0)
                else -> "Check Steam"
            }

            val categoryLabel = when (category.lowercase()) {
                "specials" -> if (discount > 0) "-$discount%" else "Special"
                "new", "new_releases" -> "New"
                else -> "Top Seller"
            }

            list.add(
                Game(
                    id = id,
                    title = name,
                    image = image,
                    price = priceStr,
                    tag = categoryLabel,
                    discountPercent = discount
                )
            )
        }
        return list
    }

    fun details(id: Int, region: String = "us"): GameDetail {
        val raw = doc("$BASE/api/appdetails?appids=$id&cc=$region&l=english")
        val outer = JSONObject(raw).optJSONObject(id.toString()) ?: JSONObject()
        val data = outer.optJSONObject("data") ?: JSONObject()

        val title = data.optString("name", "Steam Game #$id")
        val rawImage = data.optString("header_image", "")
        val image = if (rawImage.isNotBlank()) rawImage else bestHeaderImage(id, "")

        val rawDesc = data.optString("short_description", "")
        val cleanDesc = if (rawDesc.isNotBlank()) Jsoup.parse(rawDesc).text() else "No description provided by publisher."

        val developers = data.optJSONArray("developers")
        val devStr = if (developers != null && developers.length() > 0) {
            (0 until developers.length()).map { developers.optString(it) }.joinToString(", ")
        } else {
            "Independent"
        }

        val publishers = data.optJSONArray("publishers")
        val pubStr = if (publishers != null && publishers.length() > 0) {
            (0 until publishers.length()).map { publishers.optString(it) }.joinToString(", ")
        } else {
            "Steam"
        }

        val isFree = data.optBoolean("is_free", false)
        val priceOverview = data.optJSONObject("price_overview")
        val priceStr = when {
            isFree -> "Free to Play"
            priceOverview != null -> priceOverview.optString("final_formatted", "Check Steam")
            else -> "Check Steam"
        }

        val releaseDate = data.optJSONObject("release_date")?.optString("date", "").orEmpty()

        val genresList = mutableListOf<String>()
        val genresArr = data.optJSONArray("genres")
        if (genresArr != null) {
            for (i in 0 until genresArr.length()) {
                val g = genresArr.optJSONObject(i)
                val desc = g?.optString("description")
                if (!desc.isNullOrBlank()) genresList.add(desc)
            }
        }

        return GameDetail(
            id = id,
            title = title,
            image = image,
            description = cleanDesc,
            developer = devStr,
            publisher = pubStr,
            price = priceStr,
            url = "https://store.steampowered.com/app/$id/",
            releaseDate = releaseDate,
            genres = genresList
        )
    }
}
