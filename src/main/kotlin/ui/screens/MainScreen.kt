package app.majodesk.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import app.majodesk.ui.fragments.AddActCard


@Composable
fun MainScreen() {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AddActCard { name, category, type, regularity ->
                // Заглушка: просто выводим в консоль (для десктопа работает)
                println("Добавлена активность: name=$name, category=$category, type=$type, regularity=$regularity")
                // Здесь можно будет позже добавить сохранение в ViewModel
            }
        }
    }
}