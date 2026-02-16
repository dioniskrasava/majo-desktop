package app.majodesk.ui.testgui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Checkbox
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Slider
import androidx.compose.material.Surface
import androidx.compose.material.Switch
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddBox
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Rectangle
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
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.input.pointer.pointerInput
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
            //TestGuiComponent006()     // canvas
            //TestGuiComponent007()     // canvas animation
            //TestGuiComponent008()     // Рисование путей (Path):
            //TestGuiComponent009()     // Кружок который бегает за кликом
            //TestGuiComponent010()     // длинные списки
            //TestGuiComponent011()     // вкладки
            //TestGui01()     // https://fonts.google.com/icons    КНОПКИ-ИКОНКИ
            DropdownLIST()
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

    // ОБЫЧНОЕ ПОЛЕ ВВОДА
    TextField(
        value = text,
        onValueChange = { text = it },
        label = { Text("Введите текст") }
    )

    // СКРЫТЫЕ СИВОЛЫ
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

//Рисование путей (Path):
// Я ожидал немного другого конечно =)))
@Composable
fun TestGuiComponent008(){
    Canvas(modifier = Modifier.size(200.dp)) {
        val path = Path().apply {
            moveTo(0f, 0f)
            lineTo(100f, 50f)
            quadraticBezierTo(150f, 0f, 200f, 100f)
            close()
        }

        drawPath(
            path = path,
            color = Color.Cyan,
            style = Stroke(width = 4f)
        )
    }
}

//
@Composable
fun TestGuiComponent009(){
    var circlePosition by remember { mutableStateOf(Offset(100f, 100f)) }

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures { tapOffset ->
                    circlePosition = tapOffset
                }
            }
    ) {
        drawCircle(
            color = Color.Red,
            radius = 50f,
            center = circlePosition
        )

        // Можно добавить физику, коллизии и т.д.
    }
}

// Lazy-списки (для большого количества данных)
@Composable
fun TestGuiComponent010(){
    val items = (1..1000).map { "Элемент $it" }

    LazyColumn {
        items(items) { item ->
            Text(item, modifier = Modifier.padding(8.dp))
        }
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(3) // 3 колонки
    ) {
        items(items) { item ->
            Card { Text(item) }
        }
    }
}

// вкладки
@Composable
fun TestGuiComponent011(){
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Вкладка 1", "Вкладка 2", "Вкладка 3")

    Column {
        TabRow(selectedTabIndex = selectedTab) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = { Text(title) }
                )
            }
        }

        when (selectedTab) {
            0 -> Text("Контент 1")
            1 -> Text("Контент 2")
            2 -> Text("Контент 3")
        }
    }
}

// кнопочки с иконками
// иконки можно поглядеть тут  :  https://fonts.google.com/icons
@Composable
fun TestGui01() {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            IconButton(onClick = {}) {
                Icon(Icons.Default.Rectangle, contentDescription = "Лайк")
            }

            IconButton(onClick = {}) {
                Icon(Icons.Default.Check, contentDescription = "Лайк")
            }

            IconButton(onClick = {}) {
                Icon(Icons.Default.Menu, contentDescription = "Лайк")
            }

            IconButton(onClick = {}) {
                Icon(Icons.Default.Settings, contentDescription = "Лайк")
            }

            IconButton(onClick = {}) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Лайк")
            }

            IconButton(onClick = {}) {
                Icon(Icons.Default.Apps, contentDescription = "Лайк")
            }

            IconButton(onClick = {}) {
                Icon(Icons.Default.AddBox, contentDescription = "Лайк")
            }


        }
    }
}



@Composable
fun DropdownLIST() {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            var expanded by remember { mutableStateOf(false) }

            Button(onClick = { expanded = true }) {
                Text("Options")
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                DropdownMenuItem(onClick = { /* Действие 1 */ expanded = false }) {
                    Text("Option 1")
                }
                DropdownMenuItem(onClick = { /* Действие 2 */ expanded = false }) {
                    Text("Option 2")
                }
            }

        }
    }
}