package app.majodesk

import androidx.compose.material.MaterialTheme
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import app.majodesk.data.repository.FileActRepository
import app.majodesk.ui.AppSettings
import app.majodesk.ui.screens.MainScreen
import java.awt.Dimension

fun main() = application {

    val windowState = rememberWindowState(width = 800.dp, height = 600.dp)
    val actRepository = FileActRepository() // репозиторий для работы с JSON

    Window(
        onCloseRequest = ::exitApplication,  // Закрытие приложения при закрытии окна
        title = "Majo Desktop",               // Заголовок окна
        state = windowState                  // Размер и положение окна
    ) {

        window.minimumSize = Dimension(
            AppSettings.MIN_WINDOW_WIDTH,
            AppSettings.MIN_WINDOW_HEIGHT
        )

        MaterialTheme {
            MainScreen(actRepository, actRepository)
        }
    }
}




