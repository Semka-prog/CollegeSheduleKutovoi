package com.example.collegeshedulekutovoi.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.example.collegeshedulekutovoi.utils.SharedPreferencesManager

data class FavoritesState(
    val favoriteGroups: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class FavoritesViewModel : ViewModel() {
    private val _state = MutableStateFlow(FavoritesState())
    val state: StateFlow<FavoritesState> = _state.asStateFlow()
    
    init {
        loadFavorites()
    }
    
    fun loadFavorites() {
        try {
            val favorites = SharedPreferencesManager.getFavorites()
            _state.value = FavoritesState(
                favoriteGroups = favorites.sorted(),
                isLoading = false,
                errorMessage = null
            )
        } catch (e: Exception) {
            _state.value = FavoritesState(
                errorMessage = "Ошибка загрузки избранного: ${e.message}",
                isLoading = false
            )
        }
    }
    
    fun addToFavorites(group: String) {
        try {
            SharedPreferencesManager.addToFavorites(group)
            loadFavorites()
        } catch (e: Exception) {
            _state.value = _state.value.copy(
                errorMessage = "Ошибка добавления в избранное: ${e.message}"
            )
        }
    }
    
    fun removeFromFavorites(group: String) {
        try {
            SharedPreferencesManager.removeFromFavorites(group)
            loadFavorites()
        } catch (e: Exception) {
            _state.value = _state.value.copy(
                errorMessage = "Ошибка удаления из избранного: ${e.message}"
            )
        }
    }
    
    fun clearAllFavorites() {
        try {
            SharedPreferencesManager.clearAllFavorites()
            loadFavorites()
        } catch (e: Exception) {
            _state.value = _state.value.copy(
                errorMessage = "Ошибка очистки избранного: ${e.message}"
            )
        }
    }
    
    fun isFavorite(group: String): Boolean {
        return SharedPreferencesManager.isFavorite(group)
    }
}
