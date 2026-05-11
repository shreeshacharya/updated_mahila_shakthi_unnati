package com.example.updatd_mahila_shakthi.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = PrimaryPurple,
    onPrimary = Color.White,
    primaryContainer = PrimaryPurpleLight,
    onPrimaryContainer = PrimaryPurpleDark,
    secondary = SecondaryPink,
    onSecondary = Color.White,
    secondaryContainer = SecondaryPinkLight,
    onSecondaryContainer = Color(0xFF4A0028),
    tertiary = TertiaryAmber,
    onTertiary = Color.White,
    tertiaryContainer = TertiaryAmberLight,
    background = LightBackground,
    onBackground = LightOnBackground,
    surface = LightSurface,
    onSurface = LightOnSurface,
    surfaceVariant = LightSurfaceVariant,
    error = ErrorRed,
    onError = Color.White
)

private val DarkColorScheme = darkColorScheme(
    primary = DarkPrimary,
    onPrimary = Color(0xFF4A0072),
    primaryContainer = PrimaryPurple,
    onPrimaryContainer = Color(0xFFF3E5F5),
    secondary = DarkSecondary,
    onSecondary = Color(0xFF4A0028),
    secondaryContainer = SecondaryPink,
    onSecondaryContainer = Color(0xFFFCE4EC),
    tertiary = DarkTertiary,
    onTertiary = Color(0xFF3E2723),
    tertiaryContainer = TertiaryAmber,
    background = DarkBackground,
    onBackground = DarkOnBackground,
    surface = DarkSurface,
    onSurface = DarkOnSurface,
    surfaceVariant = DarkSurfaceVariant,
    error = Color(0xFFEF5350),
    onError = Color.White
)

@Composable
fun MahilaShaktiTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}