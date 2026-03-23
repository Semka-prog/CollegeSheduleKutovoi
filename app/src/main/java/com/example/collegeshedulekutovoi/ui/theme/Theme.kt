package com.example.collegeshedulekutovoi.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

// Modern Light Color Scheme
private val LightColorScheme = lightColorScheme(
    primary = Blue40,
    onPrimary = Color.White,
    primaryContainer = Blue80,
    onPrimaryContainer = Color(0xFF001D35),
    
    secondary = BlueGrey40,
    onSecondary = Color.White,
    secondaryContainer = BlueGrey80,
    onSecondaryContainer = Color(0xFF0D1B2B),
    
    tertiary = Teal40,
    onTertiary = Color.White,
    tertiaryContainer = Teal80,
    onTertiaryContainer = Color(0xFF001F26),
    
    error = ErrorRed,
    onError = Color.White,
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410E0B),
    
    background = Color(0xFFFAFBFC),
    onBackground = Color(0xFF0D1B2B),
    
    surface = Color(0xFFFAFBFC),
    onSurface = Color(0xFF0D1B2B),
    surfaceVariant = Color(0xFFDEE3EB),
    onSurfaceVariant = Color(0xFF45474F),
    outline = Color(0xFF75777F)
)

// Modern Dark Color Scheme
private val DarkColorScheme = darkColorScheme(
    primary = Blue80,
    onPrimary = Color(0xFF003D5C),
    primaryContainer = Color(0xFF00527E),
    onPrimaryContainer = Blue80,
    
    secondary = BlueGrey80,
    onSecondary = Color(0xFF1F3343),
    secondaryContainer = Color(0xFF33474E),
    onSecondaryContainer = BlueGrey80,
    
    tertiary = Teal80,
    onTertiary = Color(0xFF003638),
    tertiaryContainer = Color(0xFF004F54),
    onTertiaryContainer = Teal80,
    
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),
    
    background = Color(0xFF0D1B2B),
    onBackground = Color(0xFFE1E3E9),
    
    surface = Color(0xFF0D1B2B),
    onSurface = Color(0xFFE1E3E9),
    surfaceVariant = Color(0xFF45474F),
    onSurfaceVariant = Color(0xFFC8CDD5),
    outline = Color(0xFF91969F)
)

@Composable
fun CollegeSheduleKutovoiTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}