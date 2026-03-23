package com.example.collegeshedulekutovoi.ui.schedule

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.collegeshedulekutovoi.data.api.RetrofitInstance
import com.example.collegeshedulekutovoi.data.dto.ScheduleByDateDto
import com.example.collegeshedulekutovoi.ui.groups.GroupSearchDropdown
import com.example.collegeshedulekutovoi.utils.getWeekDateRange
import com.example.collegeshedulekutovoi.utils.SharedPreferencesManager
import com.example.collegeshedulekutovoi.viewmodel.GroupSelectionViewModel

@Composable
fun ScheduleScreen(
    groupSelectionViewModel: GroupSelectionViewModel = viewModel()
) {
    val groupState by groupSelectionViewModel.state.collectAsState()
    
    var scheduleList by remember { mutableStateOf<List<ScheduleByDateDto>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var selectedGroup by remember { mutableStateOf<String?>(SharedPreferencesManager.getSelectedGroup()) }
    var showGroupSelection by remember { mutableStateOf(selectedGroup == null) }
    var isFavorited by remember { mutableStateOf(false) }

    LaunchedEffect(selectedGroup) {
        if (selectedGroup != null && !showGroupSelection) {
            isLoading = true
            errorMessage = null
            isFavorited = SharedPreferencesManager.isFavorite(selectedGroup!!)
            try {
                val (startDate, endDate) = getWeekDateRange()
                scheduleList = RetrofitInstance.api.getSchedule(selectedGroup!!, startDate, endDate)
            } catch (e: Exception) {
                errorMessage = "Ошибка загрузки: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    if (showGroupSelection || selectedGroup == null) {
        GroupSearchDropdown(
            searchText = groupState.searchText,
            filteredGroups = groupState.filteredGroups,
            selectedGroup = groupState.selectedGroup,
            isLoading = groupState.isLoading,
            errorMessage = groupState.errorMessage,
            onSearchTextChanged = { groupSelectionViewModel.onSearchTextChanged(it) },
            onGroupSelected = { group ->
                groupSelectionViewModel.selectGroup(group)
                selectedGroup = group
                SharedPreferencesManager.saveSelectedGroup(group)
                showGroupSelection = false
            },
            onClear = { groupSelectionViewModel.clearSearch() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
    } else {
        Column(modifier = Modifier.fillMaxSize()) {
            // Search bar at the top - full width
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(12.dp)
            ) {
                GroupSearchDropdown(
                    searchText = groupState.searchText,
                    filteredGroups = groupState.filteredGroups,
                    selectedGroup = groupState.selectedGroup,
                    isLoading = groupState.isLoading,
                    errorMessage = groupState.errorMessage,
                    onSearchTextChanged = { groupSelectionViewModel.onSearchTextChanged(it) },
                    onGroupSelected = { group ->
                        groupSelectionViewModel.selectGroup(group)
                        selectedGroup = group
                        SharedPreferencesManager.saveSelectedGroup(group)
                    },
                    onClear = { groupSelectionViewModel.clearSearch() },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Group info and favorite button
            if (selectedGroup != null) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = selectedGroup!!,
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    
                    IconButton(
                        onClick = {
                            if (isFavorited) {
                                SharedPreferencesManager.removeFromFavorites(selectedGroup!!)
                            } else {
                                SharedPreferencesManager.addToFavorites(selectedGroup!!)
                            }
                            isFavorited = !isFavorited
                        }
                    ) {
                        Icon(
                            if (isFavorited) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Добавить в избранное",
                            tint = if (isFavorited) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Schedule content
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                when {
                    isLoading -> CircularProgressIndicator()
                    errorMessage != null -> Text(errorMessage!!)
                    else -> ScheduleList(schedule = scheduleList)
                }
            }
        }
    }
}
