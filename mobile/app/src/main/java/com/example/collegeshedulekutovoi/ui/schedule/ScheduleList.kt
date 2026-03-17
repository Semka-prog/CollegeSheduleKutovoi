package com.example.collegeshedulekutovoi.ui.schedule

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.collegeshedulekutovoi.data.dto.LessonGroupPart
import com.example.collegeshedulekutovoi.data.dto.ScheduleByDateDto

@Composable
fun ScheduleList(schedule: List<ScheduleByDateDto>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(schedule) { daySchedule ->
            Text(
                text = "${daySchedule.weekday}, ${daySchedule.lessonDate}",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 4.dp)
            )
            daySchedule.lessons.forEach { lesson ->
                LessonCard(lesson = lesson)
            }
        }
    }
}

@Composable
fun LessonCard(lesson: com.example.collegeshedulekutovoi.data.dto.LessonDto) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = "Пара ${lesson.lessonNumber} (${lesson.time})",
                style = MaterialTheme.typography.labelLarge
            )
            lesson.groupParts.forEach { (part, details) ->
                if (details != null) {
                    Text(
                        text = "  [${part.name}] ${details.subject}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "      ${details.teacher}",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = "      ${details.classroom}, ${details.building}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}
