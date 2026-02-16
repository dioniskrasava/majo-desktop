package app.majodesk.ui.fragments

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenu
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.majodesk.domain.model.ActCategory

/**
 * Карточка добавления активности
 * */
@Composable
fun AddActField(){

    /*
    val id: Long,
    val name: String,
    val category: ActCategory = ActCategory.ANOTHER,
    val type: ActType = ActType.ACTION,
    val regularity: Boolean = true,
    * */

    var name by remember { mutableStateOf("") } // состояние поля ввода названия
    var selectedCategory by remember {mutableStateOf(ActCategory.ANOTHER)}


    TextField(
        value = name,
        onValueChange = { name = it },
        label = { Text("Введите название активности") }
    )

    DropdownMenuCustom()

    ButtonAdd()


}


/**
 * Кнопка добавления активности
 * */
@Composable
fun ButtonAdd(){
    Box(
        modifier = Modifier
            .size(48.dp)
            .shadow(8.dp, CircleShape)   // тень только на внешний бокс
    ) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .clip(CircleShape)
                .background(Color(0xFF5390A3))
                .clickable { /* действие */ },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Добавить",
                tint = Color.White
            )
        }
    }
}

@Composable
fun DropdownMenuCustom(){

    var expanded by remember { mutableStateOf(false) }

    Box {

        Button(onClick = {expanded = true},
            shape = RectangleShape,     // прямоугольная кнопка
            colors = ButtonDefaults.buttonColors(
                 Color(0xFF5390A3)   // цвет фона кнопки
            ),
            modifier = Modifier.padding(10.dp).background(Color(0xFF5390A3))
        ){ Text("Button 1", fontSize = 28.sp) }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            Text("Скопировать", fontSize=18.sp, modifier = Modifier.padding(10.dp))
            Text("Вставить", fontSize=18.sp, modifier = Modifier.padding(10.dp))
            Divider()
            Text("Настройки", fontSize=18.sp, modifier = Modifier.padding(10.dp))
        }
    }
}

