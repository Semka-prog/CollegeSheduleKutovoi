package com.example.collegeshedulekutovoi.ui.schedule

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.collegeshedulekutovoi.data.api.RetrofitInstance
import com.example.collegeshedulekutovoi.data.dto.ScheduleByDateDto
import com.example.collegeshedulekutovoi.utils.getWeekDateRange

@Composable
fun ScheduleScreen() {
    var scheduleList by remember { mutableStateOf<List<ScheduleByDateDto>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        isLoading = true
        errorMessage = null
        try {
            val (startDate, endDate) = getWeekDateRange()
            scheduleList = RetrofitInstance.api.getSchedule("ИС-12", startDate, endDate)
        } catch (e: Exception) {
            errorMessage = "Ошибка загрузки: ${e.message}"
        } finally {
            isLoading = false
        }
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        when {
            isLoading -> CircularProgressIndicator()
            errorMessage != null -> Text(errorMessage!!)
            else -> ScheduleList(schedule = scheduleList)
        }
    }
}
