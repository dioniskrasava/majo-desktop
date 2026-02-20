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
import app.majodesk.ui.localization.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCategoryDialog(
    onDismiss: () -> Unit,
    onConfirm: (ActCategory) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var selectedIconName by remember { mutableStateOf("category") }
    var selectedColorHex by remember { mutableStateOf("#9E9E9E") }

    // Пары (имя иконки, ключ локализации)
    val iconOptions = listOf(
        "fitness_center" to "icon_fitness_center",
        "menu_book" to "icon_menu_book",
        "category" to "icon_category",
        "sports_tennis" to "icon_sports_tennis"
    )
    // Пары (hex, ключ локализации)
    val colorOptions = listOf(
        "#4CAF50" to "color_green",
        "#2196F3" to "color_blue",
        "#9E9E9E" to "color_grey",
        "#F44336" to "color_red",
        "#FFC107" to "color_yellow",
        "#9C27B0" to "color_purple"
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource("new_category")) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(stringResource("name")) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                IconDropdown(
                    selectedIconName = selectedIconName,
                    onIconSelected = { selectedIconName = it },
                    iconOptions = iconOptions
                )
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
                Text(stringResource("add"))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource("cancel"))
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
            value = stringResource(iconOptions.find { it.first == selectedIconName }?.second ?: "icon_category"),
            onValueChange = {},
            readOnly = true,
            label = { Text(stringResource("icon")) },
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
            iconOptions.forEach { (iconName, displayKey) ->
                DropdownMenuItem(
                    text = { Text(stringResource(displayKey)) },
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
            value = stringResource(colorOptions.find { it.first == selectedColorHex }?.second ?: "color_grey"),
            onValueChange = {},
            readOnly = true,
            label = { Text(stringResource("color")) },
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
            colorOptions.forEach { (colorHex, displayKey) ->
                DropdownMenuItem(
                    text = { Text(stringResource(displayKey)) },
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