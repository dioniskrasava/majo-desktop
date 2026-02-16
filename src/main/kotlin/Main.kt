package app.majodesk

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import app.majodesk.cli.mainCli
import app.majodesk.ui.screens.MainScreen
import app.majodesk.ui.testgui.AppContentTestGui

fun main() = application {


    //mainCli() // подключаем консольное подприложение

    val windowState = rememberWindowState(width = 800.dp, height = 600.dp)

    Window(
        onCloseRequest = ::exitApplication,  // Закрытие приложения при закрытии окна
        title = "Majo Desktop",               // Заголовок окна
        state = windowState                  // Размер и положение окна
    ) {
        MaterialTheme {
            //AppContentTestGui() // for testing different gui components desktop compose
            MainScreen()
        }
    }
}




