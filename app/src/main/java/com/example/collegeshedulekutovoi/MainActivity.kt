package com.example.collegeshedulekutovoi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.collegeshedulekutovoi.ui.MainScreen
import com.example.collegeshedulekutovoi.ui.theme.CollegeSheduleKutovoiTheme
import com.example.collegeshedulekutovoi.utils.SharedPreferencesManager

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Инициализация SharedPreferencesManager
        SharedPreferencesManager.init(this)
        
        enableEdgeToEdge()
        setContent {
            CollegeSheduleKutovoiTheme {
                MainScreen()
            }
        }
    }
}