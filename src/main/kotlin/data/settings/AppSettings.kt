package app.majodesk.data.settings

import kotlinx.serialization.Serializable
import app.majodesk.presentation.theme.ThemeMode
import app.majodesk.presentation.localization.Lang

@Serializable
data class AppSettings(
    val themeMode: ThemeMode = ThemeMode.LIGHT,
    val language: Lang = Lang.RU,
    val cardBackgroundColor: String? = null,
    val cardTitleColor: String? = null,
    val cardSubtitleColor: String? = null,
    val cardPaddingDp: Int? = null
)