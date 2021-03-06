package com.example.remindme.ui.theme

import android.annotation.SuppressLint
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.LiveData

@SuppressLint("ConflictingOnColor")
private val DarkColorPalette = darkColors(
    primary = Teal300,
    primaryVariant = Teal300,
    secondary = Teal200,
    surface = Teal500,
    onPrimary = Color.Black,
    onSurface = Color.Black,
    onSecondary = Color.Black,
    onBackground = Color.Black
)

private val LightColorPalette = lightColors(
    primary = Purple600,
    primaryVariant = Purple600,
    secondary = Purple200,
    surface = Purple60,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onSurface = Color.Black,
//    isLight = true


    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@Composable
fun RemindMeTheme(
    darkTheme: State<Boolean>,
    //darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit) {
    val colors = if (darkTheme.value) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}