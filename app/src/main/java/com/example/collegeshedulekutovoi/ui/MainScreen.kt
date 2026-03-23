package com.example.collegeshedulekutovoi.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.collegeshedulekutovoi.ui.navigation.BottomNavigationBar
import com.example.collegeshedulekutovoi.ui.navigation.NavigationItem
import com.example.collegeshedulekutovoi.ui.profile.ProfileScreen
import com.example.collegeshedulekutovoi.ui.favorites.FavoritesScreen
import com.example.collegeshedulekutovoi.ui.schedule.ScheduleScreen
import com.example.collegeshedulekutovoi.utils.SharedPreferencesManager

@Composable
fun MainScreen() {
    var currentNavigationItem by remember { mutableStateOf(NavigationItem.Schedule) }
    
    Column(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.weight(1f)) {
            when (currentNavigationItem) {
                NavigationItem.Schedule -> ScheduleScreen()
                NavigationItem.Favorites -> FavoritesScreen(
                    onGroupSelected = { group ->
                        SharedPreferencesManager.saveSelectedGroup(group)
                        currentNavigationItem = NavigationItem.Schedule
                    },
                    onBack = {}
                )
                NavigationItem.Profile -> ProfileScreen()
            }
        }
        
        BottomNavigationBar(
            selectedItem = currentNavigationItem,
            onItemSelected = { currentNavigationItem = it }
        )
    }
}
