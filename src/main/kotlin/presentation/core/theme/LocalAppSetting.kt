package app.majodesk.presentation.core.theme

import androidx.compose.runtime.compositionLocalOf
import app.majodesk.data.settings.AppSettings

val LocalAppSettings = compositionLocalOf { AppSettings() }