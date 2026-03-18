package com.example.collegeshedulekutovoi.ui.schedule

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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

    LaunchedEffect(selectedGroup) {
        if (selectedGroup != null && !showGroupSelection) {
            isLoading = true
            errorMessage = null
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
            // Заголовок с информацией о группе
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Расписание для группы: $selectedGroup",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.align(Alignment.CenterStart)
                )
                IconButton(
                    onClick = { showGroupSelection = true },
                    modifier = Modifier.align(Alignment.CenterEnd)
                ) {
                    Icon(Icons.Default.Edit, contentDescription = "Изменить группу")
                }
            }

            // Содержимое расписания
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
