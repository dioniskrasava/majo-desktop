package app.majodesk.ui.testgui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Checkbox
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Slider
import androidx.compose.material.Surface
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Email
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun AppContentTestGui(){
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            //TestGuiComponent001()
            //TestGuiComponent002()
            //TestGuiComponent003()
            //TestGuiComponent004()
            //TestGuiComponent005()
            //TestGuiComponent006()     //canvas
            TestGuiComponent007()     //canvas animation
            //TestGuiComponent008()
            //TestGuiComponent009()
            //TestGuiComponent010()
        }
    }
}


// Тексты
@Composable
fun TestGuiComponent001(){
    Text("Привет мир!")  // Простой текст
    Text("Стили", style = MaterialTheme.typography.h4)  // С заголовком
    Text(
        "Кликни меня",
        modifier = Modifier.clickable { println("Клик!") }
    )
}


// Кнопки и интерактивные элементы
@Composable
fun TestGuiComponent002(){
    Button(onClick = { /* действие */ }) {
        Text("Нажми меня")
    }

    OutlinedButton(onClick = {}) {
        Text("Контурная кнопка")
    }

    IconButton(onClick = {}) {
        Icon(Icons.Default.Favorite, contentDescription = "Лайк")
    }

    Checkbox(checked = true, onCheckedChange = {})
    Switch(checked = true, onCheckedChange = {})
    Slider(value = 0.5f, onValueChange = {})
}


// Поля ввода
@Composable
fun TestGuiComponent003(){

    var text by remember { mutableStateOf("") }

    TextField(
        value = text,
        onValueChange = { text = it },
        label = { Text("Введите текст") }
    )

    OutlinedTextField(
        value = text,
        onValueChange = { text = it },
        label = { Text("Пароль") },
        visualTransformation = PasswordVisualTransformation()
    )
}

// Макеты и контейнеры:
@Composable
fun TestGuiComponent004(){
    // Вертикальное расположение
    Column {
        Text("Сверху")
        Text("Внизу")
    }

// Горизонтальное расположение
    Row {
        Text("Слева")
        Text("Справа")
    }

// Наложение элементов
    Box {
        Text("Сзади")
        Text("Спереди", modifier = Modifier.align(Alignment.Center))
    }

// Карточка
    Card(elevation = 4.dp) {
        Text("Карточка с тенью")
    }
}

// Иконки (тысячи бесплатных иконок Material):
@Composable
fun TestGuiComponent005(){
    Icon(Icons.Default.Home, "Домой")
    Icon(Icons.Default.Favorite, "Избранное")
    Icon(Icons.Default.Settings, "Настройки")
    Icon(Icons.Filled.Star, "Звезда")
    Icon(Icons.Outlined.Email, "Почта")
}

//
@Composable
fun TestGuiComponent006(){
    Canvas(modifier = Modifier.size(200.dp)) {
        // Прямоугольник
        drawRect(color = Color.Red, size = Size(100f, 50f))

        // Круг
        drawCircle(color = Color.Blue, radius = 40f, center = Offset(150f, 150f))

        // Линия
        drawLine(
            color = Color.Green,
            start = Offset(0f, 0f),
            end = Offset(200f, 200f),
            strokeWidth = 5f
        )

        // Овал
        drawOval(color = Color.Magenta, size = Size(80f, 40f))

        // Дуга/сектор
        drawArc(
            color = Color.Yellow,
            startAngle = 0f,
            sweepAngle = 270f,
            useCenter = true,
            size = Size(100f, 100f)
        )
    }
}

//Анимации на Canvas:
@Composable
fun TestGuiComponent007(){
    var angle by remember { mutableStateOf(0f) }

    LaunchedEffect(Unit) {
        while (true) {
            angle += 2f
            delay(16) // ~60 FPS
        }
    }

    Canvas(modifier = Modifier.size(100.dp)) {
        rotate(angle) {
            drawRect(
                color = Color.Blue,
                topLeft = Offset(-25f, -25f),
                size = Size(50f, 50f)
            )
        }
    }
}

//
@Composable
fun TestGuiComponent008(){
}

//
@Composable
fun TestGuiComponent009(){
}

//
@Composable
fun TestGuiComponent010(){
}