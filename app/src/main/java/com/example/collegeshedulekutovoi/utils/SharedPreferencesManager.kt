package com.example.collegeshedulekutovoi.utils

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object SharedPreferencesManager {
    private const val PREF_NAME = "CollegeShedulePreferences"
    private const val KEY_SELECTED_GROUP = "selected_group"
    private const val KEY_FAVORITE_GROUPS = "favorite_groups"
    
    private lateinit var preferences: SharedPreferences
    private val gson = Gson()
    
    fun init(context: Context) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }
    
    fun saveSelectedGroup(group: String) {
        preferences.edit().putString(KEY_SELECTED_GROUP, group).apply()
    }
    
    fun getSelectedGroup(): String? {
        return preferences.getString(KEY_SELECTED_GROUP, null)
    }
    
    fun clearSelectedGroup() {
        preferences.edit().remove(KEY_SELECTED_GROUP).apply()
    }
    
    // Favorites management
    fun addToFavorites(group: String) {
        val favorites = getFavorites().toMutableList()
        if (!favorites.contains(group)) {
            favorites.add(group)
            saveFavorites(favorites)
        }
    }
    
    fun removeFromFavorites(group: String) {
        val favorites = getFavorites().toMutableList()
        favorites.remove(group)
        saveFavorites(favorites)
    }
    
    fun getFavorites(): List<String> {
        val json = preferences.getString(KEY_FAVORITE_GROUPS, "[]")
        return try {
            val type = object : TypeToken<List<String>>() {}.type
            gson.fromJson(json, type) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }
    
    fun isFavorite(group: String): Boolean {
        return getFavorites().contains(group)
    }
    
    private fun saveFavorites(favorites: List<String>) {
        val json = gson.toJson(favorites)
        preferences.edit().putString(KEY_FAVORITE_GROUPS, json).apply()
    }
    
    fun clearAllFavorites() {
        preferences.edit().remove(KEY_FAVORITE_GROUPS).apply()
    }
}
