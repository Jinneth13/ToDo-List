package com.example.todo.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = BluePrimary,
    secondary = FacebookBlue,
    tertiary = GoogleRed,
    background = White,
    onPrimary = White,
    onSecondary = White,
    onBackground = Black
)

private val DarkColors = darkColorScheme(
    primary = BluePrimary,
    secondary = FacebookBlue,
    tertiary = GoogleRed,
    background = Color(0xFF121212),
    onPrimary = White,
    onSecondary = White,
    onBackground = White
)

@Composable
fun ToDoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography(),
        content = content
    )
}