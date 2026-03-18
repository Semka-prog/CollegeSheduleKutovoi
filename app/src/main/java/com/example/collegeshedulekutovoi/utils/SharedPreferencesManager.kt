package com.example.collegeshedulekutovoi.utils

import android.content.Context
import android.content.SharedPreferences

object SharedPreferencesManager {
    private const val PREF_NAME = "CollegeShedulePreferences"
    private const val KEY_SELECTED_GROUP = "selected_group"
    
    private lateinit var preferences: SharedPreferences
    
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
}
