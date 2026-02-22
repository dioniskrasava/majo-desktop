package app.majodesk.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

fun appColorScheme(mode: ThemeMode): ColorScheme {
    return when (mode) {
        ThemeMode.LIGHT -> lightColorScheme(
            primary = Color(0xFFDC5959),
            secondary = Color(0xFF03DAC5),
            background = Color(0xFFFFFBFE),
            surface = Color(0xFFFFFBFE),
            surfaceVariant = Color(0xFFD5C7C7),
            onPrimary = Color.White,
            onSecondary = Color.Black,
            onBackground = Color.Black,
            onSurface = Color.Black
            // остальные цвета можно не указывать — будут использованы значения по умолчанию
        )
        ThemeMode.DARK -> darkColorScheme(
            primary = Color(0xFF447FCD),
            secondary = Color(0xFF03DAC5),
            background = Color(0xFF121212),
            surface = Color(0xFF2D2D2D),
            surfaceVariant = Color(0xFF454545),
            onPrimary = Color.Black,
            onSecondary = Color.Black,
            onBackground = Color.White,
            onSurface = Color.White
        )
    }
}