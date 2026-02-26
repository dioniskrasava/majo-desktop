package app.majodesk.ui.theme

import androidx.compose.runtime.compositionLocalOf
import app.majodesk.data.settings.AppSettings

val LocalAppSettings = compositionLocalOf { AppSettings() }