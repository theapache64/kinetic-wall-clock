package com.theapache64.klokk.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val LightTheme = lightColors(
    primary = Color(0xFF2A2F4F),
    secondary = Color(0xFF917FB3),
    background = Color(0xFFE5BEEC),
    surface = Color(0xFFFDE2F3),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color.Black,
    onSurface = Color.Black,
)

val DarkTheme = darkColors(
    primary = Color(0xFF1F6E8C),
    secondary = Color(0xFF455A64),
    background = Color(0xFF263238),
    surface = Color(0xFF212121),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White,
)

@Composable
fun KlokkTheme(
    isDark: Boolean = false,
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colors = if (isDark) DarkTheme else LightTheme,
        typography = KlokkTypography
    ) {
        Surface {
            content()
        }
    }
}