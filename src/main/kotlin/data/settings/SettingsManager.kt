package app.majodesk.data.settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import app.majodesk.presentation.core.localization.Lang
import app.majodesk.presentation.core.theme.ThemeMode

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

    fun updateCardBackgroundColor(hex: String?) {
        settings = settings.copy(cardBackgroundColor = hex)
        repository.save(settings)
    }

    fun updateCardTitleColor(hex: String?) {
        settings = settings.copy(cardTitleColor = hex)
        repository.save(settings)
    }

    fun updateCardSubtitleColor(hex: String?) {
        settings = settings.copy(cardSubtitleColor = hex)
        repository.save(settings)
    }

    fun updateCardPadding(dp: Int?) {
        settings = settings.copy(cardPaddingDp = dp)
        repository.save(settings)
    }
}