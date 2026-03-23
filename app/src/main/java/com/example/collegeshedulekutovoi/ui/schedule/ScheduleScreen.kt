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
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.collegeshedulekutovoi.data.api.RetrofitInstance
import com.example.collegeshedulekutovoi.data.dto.ScheduleByDateDto
import com.example.collegeshedulekutovoi.ui.groups.GroupSelectionScreen
import com.example.collegeshedulekutovoi.utils.getWeekDateRange
import com.example.collegeshedulekutovoi.utils.SharedPreferencesManager

@Composable
fun ScheduleScreen() {
    var scheduleList by remember { mutableStateOf<List<ScheduleByDateDto>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var selectedGroup by remember { mutableStateOf<String?>(SharedPreferencesManager.getSelectedGroup()) }
    var showGroupSelection by remember { mutableStateOf(selectedGroup == null) }
    var isFavorited by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf(selectedGroup ?: "") }

    LaunchedEffect(selectedGroup) {
        if (selectedGroup != null && !showGroupSelection) {
            isLoading = true
            errorMessage = null
            isFavorited = SharedPreferencesManager.isFavorite(selectedGroup!!)
            searchText = selectedGroup!!
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
        GroupSelectionScreen(
            onGroupSelected = { group ->
                selectedGroup = group
                SharedPreferencesManager.saveSelectedGroup(group)
                showGroupSelection = false
            }
        )
    } else {
        Column(modifier = Modifier.fillMaxSize()) {
            // Search bar at the top
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    modifier = Modifier
                        .weight(1f)
                        .background(
                            color = MaterialTheme.colorScheme.surfaceContainer,
                            shape = RoundedCornerShape(12.dp)
                        ),
                    placeholder = { Text("Поиск группы...") },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    trailingIcon = {
                        if (searchText.isNotEmpty()) {
                            IconButton(onClick = { searchText = "" }) {
                                Icon(
                                    Icons.Default.Clear,
                                    contentDescription = "Очистить",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    },
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        focusedBorderColor = MaterialTheme.colorScheme.primary
                    )
                )
                
                // Edit button
                IconButton(onClick = { showGroupSelection = true }) {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = "Изменить группу",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
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
