package app.majodesk

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import app.majodesk.data.repository.FileActRecordRepository
import app.majodesk.data.repository.FileActRepository
import app.majodesk.presentation.AppSettings
import app.majodesk.presentation.core.localization.LocalLocalizationManager
import app.majodesk.presentation.core.localization.LocalizationManager
import app.majodesk.presentation.features.main.MainScreen
import app.majodesk.presentation.core.theme.appColorScheme
import java.awt.Dimension
import app.majodesk.data.settings.FileSettingsRepository
import app.majodesk.data.settings.SettingsManager
import app.majodesk.presentation.core.theme.LocalAppSettings

fun main() = application {
    val windowState = rememberWindowState(width = AppSettings.WINDOW_WIDTH, height = AppSettings.WINDOW_HEIGHT)
    //var themeMode by remember { mutableStateOf(ThemeMode.LIGHT) }
    val settingsManager = remember { SettingsManager(FileSettingsRepository()) }
    val localizationManager = remember { LocalizationManager() }

    val actRepository = FileActRepository()
    val recordRepository = FileActRecordRepository()

    // Синхронизация языка при изменении настроек
    LaunchedEffect(settingsManager.settings.language) {
        localizationManager.setLanguage(settingsManager.settings.language)
    }

    Window(
        onCloseRequest = ::exitApplication,
        title = "Majo Desktop",
        state = windowState
    ) {
        window.minimumSize = Dimension(
            AppSettings.MIN_WINDOW_WIDTH,
            AppSettings.MIN_WINDOW_HEIGHT
        )
        CompositionLocalProvider(LocalLocalizationManager provides localizationManager,
                                           LocalAppSettings provides settingsManager.settings
            ) { // <-- обёртка
            MaterialTheme( appColorScheme(settingsManager.settings.themeMode)) {
                MainScreen(
                    actRepository = actRepository,
                    recordRepository = recordRepository,
                    settingsManager = settingsManager
                )
            }
        }
    }
}



