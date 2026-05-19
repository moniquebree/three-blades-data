package com.threeblades.kuro.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Ink = Color(0xFF0A0A0D)
val Night = Color(0xFF14141C)
val Mist = Color(0xFF1F1F2B)
val Moon = Color(0xFFE8E4D8)
val Ember = Color(0xFFD98C4A)
val Jade = Color(0xFF7FAE8F)
val Whisper = Color(0xFF8A8A98)
val ShuIro = Color(0xFFC73E3A)

private val KuroColorScheme = darkColorScheme(
    primary = Ember,
    onPrimary = Ink,
    secondary = ShuIro,
    background = Ink,
    onBackground = Moon,
    surface = Night,
    onSurface = Moon,
)

@Composable
fun KuroTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = KuroColorScheme,
        content = content,
    )
}
