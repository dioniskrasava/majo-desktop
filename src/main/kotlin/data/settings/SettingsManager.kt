package app.majodesk.data.settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import app.majodesk.ui.localization.Lang
import app.majodesk.ui.theme.ThemeMode

class SettingsManager(
    private val repository: SettingsRepository
) {
    var settings by mutableStateOf(repository.load())
        private set

    fun updateTheme(themeMode: ThemeMode) {
        settings = settings.copy(themeMode = themeMode)
        repository.save(settings)
    }

    fun updateLanguage(language: Lang) {
        settings = settings.copy(language = language)
        repository.save(settings)
    }
}