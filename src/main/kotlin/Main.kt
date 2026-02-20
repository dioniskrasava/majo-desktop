package app.majodesk

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import app.majodesk.data.repository.FileActRepository
import app.majodesk.ui.AppSettings
import app.majodesk.ui.localization.LocalLocalizationManager
import app.majodesk.ui.localization.LocalizationManager
import app.majodesk.ui.screens.MainScreen
import app.majodesk.ui.theme.ThemeMode
import app.majodesk.ui.theme.appColorScheme
import java.awt.Dimension

fun main() = application {
    val windowState = rememberWindowState(width = 800.dp, height = 600.dp)
    val actRepository = FileActRepository()
    var themeMode by remember { mutableStateOf(ThemeMode.LIGHT) }
    val localizationManager = remember { LocalizationManager() } // <-- добавлено

    Window(
        onCloseRequest = ::exitApplication,
        title = "Majo Desktop",
        state = windowState
    ) {
        window.minimumSize = Dimension(
            AppSettings.MIN_WINDOW_WIDTH,
            AppSettings.MIN_WINDOW_HEIGHT
        )
        CompositionLocalProvider(LocalLocalizationManager provides localizationManager) { // <-- обёртка
            MaterialTheme( appColorScheme(themeMode)) {
                MainScreen(
                    repository = actRepository,
                    themeMode = themeMode,
                    onThemeToggle = { themeMode = if (themeMode == ThemeMode.LIGHT) ThemeMode.DARK else ThemeMode.LIGHT }
                )
            }
        }
    }
}



