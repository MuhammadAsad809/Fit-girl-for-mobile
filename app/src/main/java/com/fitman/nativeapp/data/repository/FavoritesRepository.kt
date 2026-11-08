package com.fitman.nativeapp.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.fitman.nativeapp.data.model.Game
import org.json.JSONArray
import org.json.JSONObject

class FavoritesRepository(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("fitman_favorites", Context.MODE_PRIVATE)

    fun getFavorites(): List<Game> {
        val raw = prefs.getString("favorites_list", null) ?: return emptyList()
        return try {
            val arr = JSONArray(raw)
            val list = mutableListOf<Game>()
            for (i in 0 until arr.length()) {
                val o = arr.optJSONObject(i) ?: continue
                list.add(
                    Game(
                        id = o.optInt("id"),
                        title = o.optString("title"),
                        image = o.optString("image"),
                        price = o.optString("price"),
                        url = o.optString("url"),
                        tag = o.optString("tag")
                    )
                )
            }
            list
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun isFavorite(id: Int): Boolean {
        return getFavorites().any { it.id == id }
    }

    fun toggleFavorite(game: Game): Boolean {
        val current = getFavorites().toMutableList()
        val index = current.indexOfFirst { it.id == game.id }
        val isNowFavorite = if (index >= 0) {
            current.removeAt(index)
            false
        } else {
            current.add(0, game)
            true
        }
        saveFavorites(current)
        return isNowFavorite
    }

    fun removeFavorite(id: Int) {
        val current = getFavorites().toMutableList()
        val filtered = current.filterNot { it.id == id }
        saveFavorites(filtered)
    }

    private fun saveFavorites(list: List<Game>) {
        val arr = JSONArray()
        for (g in list) {
            val o = JSONObject()
            o.put("id", g.id)
            o.put("title", g.title)
            o.put("image", g.image)
            o.put("price", g.price)
            o.put("url", g.url)
            o.put("tag", g.tag)
            arr.put(o)
        }
        prefs.edit().putString("favorites_list", arr.toString()).apply()
    }
}
