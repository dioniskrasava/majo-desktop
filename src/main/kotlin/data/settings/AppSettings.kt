package app.majodesk.data.settings

import kotlinx.serialization.Serializable
import app.majodesk.ui.theme.ThemeMode
import app.majodesk.ui.localization.Lang

@Serializable
data class AppSettings(
    val themeMode: ThemeMode = ThemeMode.LIGHT,
    val language: Lang = Lang.RU
)