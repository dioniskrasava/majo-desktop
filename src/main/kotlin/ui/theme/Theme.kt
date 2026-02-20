package app.majodesk.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

fun appColorScheme(mode: ThemeMode): ColorScheme {
    return when (mode) {
        ThemeMode.LIGHT -> lightColorScheme(
            primary = Color(0xFF238AD9),
            secondary = Color(0xFF03DAC5),
            background = Color(0xFFFFFBFE),
            surface = Color(0xFFFFFBFE),
            onPrimary = Color.White,
            onSecondary = Color.Black,
            onBackground = Color.Black,
            onSurface = Color.Black
            // остальные цвета можно не указывать — будут использованы значения по умолчанию
        )
        ThemeMode.DARK -> darkColorScheme(
            primary = Color(0xFF5993DB),
            secondary = Color(0xFF03DAC5),
            background = Color(0xFF121212),
            surface = Color(0xFF0C1021),
            onPrimary = Color.Black,
            onSecondary = Color.Black,
            onBackground = Color.White,
            onSurface = Color.White
        )
    }
}