package app.majodesk.ui.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.filled.ShowChart
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import app.majodesk.domain.model.Act
import app.majodesk.domain.model.ActCategory
import app.majodesk.domain.repository.ActRepository
import app.majodesk.domain.repository.CategoryRepository
import app.majodesk.ui.fragments.ActList
import app.majodesk.ui.fragments.AddActCard
import app.majodesk.ui.fragments.AddCategoryDialog // создайте этот компонент
import app.majodesk.ui.fragments.ThemeSwitch
import app.majodesk.ui.localization.stringResource
import app.majodesk.ui.navigation.Screen
import app.majodesk.ui.theme.ThemeMode

@Composable
fun <T> MainScreen(
    repository: T,
    themeMode: ThemeMode,
    onThemeToggle: () -> Unit
) where T : ActRepository, T : CategoryRepository {

    var currentScreen by remember { mutableStateOf<Screen>(Screen.Activities) }

    Row(modifier = Modifier.fillMaxSize()) {


        // Левая навигационная панель
        NavigationRail(
            header = {
                // Можно добавить логотип или заголовок
            },
            modifier = Modifier.border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant,
                shape = RoundedCornerShape(topEnd = 8.dp, bottomEnd = 8.dp, topStart = 8.dp, bottomStart = 8.dp),
            ),
            containerColor = MaterialTheme.colorScheme.surfaceVariant, // вместо primary
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant, // если нужно
        ) {
            NavigationRailItem(
                selected = currentScreen == Screen.Activities,
                onClick = { currentScreen = Screen.Activities },
                icon = { Icon(Icons.Default.List, contentDescription = "Активности") },
                label = { Text(stringResource("activities")) }
            )
            NavigationRailItem(
                selected = currentScreen == Screen.Statistics,
                onClick = { currentScreen = Screen.Statistics },
                icon = { Icon(Icons.Default.ShowChart, contentDescription = "Статистика") },
                label = { Text(stringResource("statistics")) }
            )
            NavigationRailItem(
                selected = currentScreen == Screen.Settings,
                onClick = { currentScreen = Screen.Settings },
                icon = { Icon(Icons.Default.Settings, contentDescription = "Настройки") },
                label = { Text(stringResource("settings")) }
            )
        }

        // Контентная область – отображаем нужный экран
        Surface(modifier = Modifier.weight(1f)) {
            when (currentScreen) {
                Screen.Activities -> ActivitiesScreen(
                    repository = repository,
                    // Передаём также список категорий, но лучше использовать репозиторий напрямую
                )
                Screen.Statistics -> StatisticsScreen()
                Screen.Settings -> SettingsScreen(
                    themeMode = themeMode,
                    onThemeToggle = onThemeToggle
                )
            }
        }
    }
}