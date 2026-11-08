package com.fitman.nativeapp.data.repository

import android.content.Context
import android.content.SharedPreferences
import org.json.JSONArray

class SettingsRepository(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("fitman_settings", Context.MODE_PRIVATE)

    var specularEnabled: Boolean
        get() = prefs.getBoolean("specular_effects", true)
        set(value) = prefs.edit().putBoolean("specular_effects", value).apply()

    var glassBlurEnabled: Boolean
        get() = prefs.getBoolean("glass_blur", true)
        set(value) = prefs.edit().putBoolean("glass_blur", value).apply()

    var storeRegion: String
        get() = prefs.getString("store_region", "us") ?: "us"
        set(value) = prefs.edit().putString("store_region", value).apply()

    fun getRecentSearches(): List<String> {
        val raw = prefs.getString("recent_searches", null) ?: return emptyList()
        return try {
            val arr = JSONArray(raw)
            (0 until arr.length()).map { arr.getString(it) }
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun addRecentSearch(query: String) {
        val trimmed = query.trim()
        if (trimmed.isEmpty()) return
        val current = getRecentSearches().toMutableList()
        current.remove(trimmed)
        current.add(0, trimmed)
        val capped = current.take(8)
        val arr = JSONArray(capped)
        prefs.edit().putString("recent_searches", arr.toString()).apply()
    }

    fun clearRecentSearches() {
        prefs.edit().remove("recent_searches").apply()
    }
}
