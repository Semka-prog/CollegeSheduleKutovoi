package com.example.collegeshedulekutovoi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.collegeshedulekutovoi.ui.schedule.ScheduleScreen
import com.example.collegeshedulekutovoi.ui.theme.CollegeSheduleKutovoiTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CollegeSheduleKutovoiTheme {
                ScheduleScreen()
            }
        }
    }
}