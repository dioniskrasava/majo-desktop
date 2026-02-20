package app.majodesk.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.majodesk.ui.fragments.ThemeSwitch
import app.majodesk.ui.theme.ThemeMode

@Composable
fun SettingsScreen(
    themeMode: ThemeMode,
    onThemeToggle: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(text = "Настройки", style = MaterialTheme.typography.headlineSmall)
        ThemeSwitch(themeMode = themeMode, onToggle = onThemeToggle)
        // Здесь можно добавить другие настройки
    }
}