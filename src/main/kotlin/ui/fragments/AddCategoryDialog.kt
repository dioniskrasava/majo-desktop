package app.majodesk.ui.fragments

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import app.majodesk.domain.model.ActCategory
import app.majodesk.ui.colorFromHex
import app.majodesk.ui.iconFromName

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCategoryDialog(
    onDismiss: () -> Unit,
    onConfirm: (ActCategory) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var selectedIconName by remember { mutableStateOf("category") }
    var selectedColorHex by remember { mutableStateOf("#9E9E9E") }

    // Список доступных иконок (имя → отображаемое имя)
    val iconOptions = listOf(
        "fitness_center" to "Спорт",
        "menu_book" to "Образование",
        "category" to "Категория",
        "sports_tennis" to "Теннис"
        // можно добавить другие иконки, если они есть в iconFromName
    )

    // Список предопределённых цветов (hex → название)
    val colorOptions = listOf(
        "#4CAF50" to "Зелёный",
        "#2196F3" to "Синий",
        "#9E9E9E" to "Серый",
        "#F44336" to "Красный",
        "#FFC107" to "Жёлтый",
        "#9C27B0" to "Фиолетовый"
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Новая категория") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                // Поле для названия
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Название") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                // Выбор иконки
                IconDropdown(
                    selectedIconName = selectedIconName,
                    onIconSelected = { selectedIconName = it },
                    iconOptions = iconOptions
                )

                // Выбор цвета
                ColorDropdown(
                    selectedColorHex = selectedColorHex,
                    onColorSelected = { selectedColorHex = it },
                    colorOptions = colorOptions
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (name.isNotBlank()) {
                        onConfirm(ActCategory(name, selectedIconName, selectedColorHex))
                    }
                },
                enabled = name.isNotBlank()
            ) {
                Text("Добавить")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IconDropdown(
    selectedIconName: String,
    onIconSelected: (String) -> Unit,
    iconOptions: List<Pair<String, String>>
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = iconOptions.find { it.first == selectedIconName }?.second ?: "",
            onValueChange = {},
            readOnly = true,
            label = { Text("Иконка") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            leadingIcon = {
                Icon(
                    imageVector = iconFromName(selectedIconName),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            iconOptions.forEach { (iconName, displayName) ->
                DropdownMenuItem(
                    text = { Text(displayName) },
                    onClick = {
                        onIconSelected(iconName)
                        expanded = false
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = iconFromName(iconName),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ColorDropdown(
    selectedColorHex: String,
    onColorSelected: (String) -> Unit,
    colorOptions: List<Pair<String, String>>
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = colorOptions.find { it.first == selectedColorHex }?.second ?: "",
            onValueChange = {},
            readOnly = true,
            label = { Text("Цвет") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            leadingIcon = {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(colorFromHex(selectedColorHex))
                )
            },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            colorOptions.forEach { (colorHex, colorName) ->
                DropdownMenuItem(
                    text = { Text(colorName) },
                    onClick = {
                        onColorSelected(colorHex)
                        expanded = false
                    },
                    leadingIcon = {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(colorFromHex(colorHex))
                        )
                    }
                )
            }
        }
    }
}