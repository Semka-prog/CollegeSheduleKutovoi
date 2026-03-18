package com.example.collegeshedulekutovoi.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.collegeshedulekutovoi.data.api.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class GroupSelectionState(
    val groups: List<String> = emptyList(),
    val filteredGroups: List<String> = emptyList(),
    val selectedGroup: String? = null,
    val searchText: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class GroupSelectionViewModel : ViewModel() {
    companion object {
        private const val TAG = "GroupSelectionViewModel"
    }
    
    private val _state = MutableStateFlow(GroupSelectionState())
    val state: StateFlow<GroupSelectionState> = _state.asStateFlow()
    
    init {
        loadGroups()
    }
    
    private fun loadGroups() {
        viewModelScope.launch {
            try {
                _state.value = _state.value.copy(
                    isLoading = true,
                    errorMessage = null
                )
                val groupDtos = RetrofitInstance.api.getAllGroups()
                val groups = groupDtos
                    .mapNotNull { 
                        if (it.name.isNotBlank()) it.name else null
                    }
                    .distinct()
                    .sorted()
                _state.value = _state.value.copy(
                    groups = groups,
                    filteredGroups = groups,
                    isLoading = false,
                    errorMessage = null
                )
            } catch (e: Exception) {
                Log.e(TAG, "Erro ao carregar grupos", e)
                _state.value = _state.value.copy(
                    errorMessage = "Ошибка загрузки групп: ${e.message}",
                    isLoading = false
                )
            }
        }
    }
    
    fun onSearchTextChanged(text: String) {
        try {
            _state.value = _state.value.copy(searchText = text)
            filterGroups(text)
        } catch (e: Exception) {
            _state.value = _state.value.copy(
                errorMessage = "Ошибка при фильтрации: ${e.message}"
            )
        }
    }
    
    private fun filterGroups(query: String) {
        try {
            val currentGroups = _state.value.groups.filter { it.isNotBlank() }
            val filtered = if (query.isBlank()) {
                currentGroups
            } else {
                currentGroups.filter {
                    it.contains(query, ignoreCase = true)
                }
            }
            _state.value = _state.value.copy(
                filteredGroups = filtered,
                errorMessage = null
            )
        } catch (e: Exception) {
            Log.e(TAG, "Erro ao filtrar grupos", e)
            _state.value = _state.value.copy(
                errorMessage = "Ошибка фильтрации: ${e.message}"
            )
        }
    }
    
    fun selectGroup(group: String) {
        try {
            _state.value = _state.value.copy(
                selectedGroup = group,
                searchText = group,
                filteredGroups = emptyList(),
                errorMessage = null
            )
        } catch (e: Exception) {
            _state.value = _state.value.copy(
                errorMessage = "Ошибка при выборе группы: ${e.message}"
            )
        }
    }
    
    fun clearSearch() {
        try {
            _state.value = _state.value.copy(
                searchText = "",
                filteredGroups = _state.value.groups,
                errorMessage = null
            )
        } catch (e: Exception) {
            _state.value = _state.value.copy(
                errorMessage = "Ошибка при очистке: ${e.message}"
            )
        }
    }
    
    fun resetSelection() {
        try {
            _state.value = _state.value.copy(
                selectedGroup = null,
                searchText = "",
                filteredGroups = _state.value.groups,
                errorMessage = null
            )
        } catch (e: Exception) {
            _state.value = _state.value.copy(
                errorMessage = "Ошибка при сбросе: ${e.message}"
            )
        }
    }
}
