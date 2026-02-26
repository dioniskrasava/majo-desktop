package app.majodesk.ui.fragments.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import app.majodesk.data.settings.SettingsManager
import app.majodesk.ui.colorFromHex
import app.majodesk.ui.localization.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomThemeDialog(
    settingsManager: SettingsManager,
    onDismiss: () -> Unit
) {
    val currentSettings = settingsManager.settings

    var cardBgColor by remember { mutableStateOf(currentSettings.cardBackgroundColor ?: "") }
    var cardTitleColor by remember { mutableStateOf(currentSettings.cardTitleColor ?: "") }
    var cardSubtitleColor by remember { mutableStateOf(currentSettings.cardSubtitleColor ?: "") }
    var cardPadding by remember { mutableStateOf(currentSettings.cardPaddingDp ?: 16) }

    // Цвета для карточек
    val cardBgOptions = listOf(
        "#c7b5b5" to "Красный",
        "#c7bfb5" to "Оранжевый",
        "#c7c5b5" to "Желтый",
        "#b5c7b6" to "Зеленый",
        "#b5c5c7" to "Голубой",
        "#b5b5c7" to "Синий",
        "#c2b5c7" to "Фиолетовый",
    )

    // Цвета для Шрифтов
    val textColorOptions = listOf(
        "#000000" to "Черный",
        "#858585" to "Серый",
        "#ffffff" to "Белый",
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Кастомизация темы") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                // Цвет фона карточки
                ColorDropdown(
                    label = "Цвет фона карточки",
                    selectedColorHex = cardBgColor,
                    onColorSelected = { cardBgColor = it },
                    colorOptions = cardBgOptions
                )

                // Цвет заголовка
                ColorDropdown(
                    label = "Цвет заголовка",
                    selectedColorHex = cardTitleColor,
                    onColorSelected = { cardTitleColor = it },
                    colorOptions = textColorOptions
                )

                // Цвет подзаголовка
                ColorDropdown(
                    label = "Цвет подзаголовка",
                    selectedColorHex = cardSubtitleColor,
                    onColorSelected = { cardSubtitleColor = it },
                    colorOptions = textColorOptions
                )

                // Отступ внутри карточки
                Text("Отступ внутри карточки: $cardPadding dp")
                Slider(
                    value = cardPadding.toFloat(),
                    onValueChange = { cardPadding = it.toInt() },
                    valueRange = 8f..32f,
                    steps = 6
                )
            }
        },
        confirmButton = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Кнопка сброса
                TextButton(
                    onClick = {
                        settingsManager.updateCardBackgroundColor(null)
                        settingsManager.updateCardTitleColor(null)
                        settingsManager.updateCardSubtitleColor(null)
                        settingsManager.updateCardPadding(null)
                        onDismiss()
                    }
                ) {
                    Text("Сбросить")
                }
                // Кнопка сохранения
                TextButton(
                    onClick = {
                        settingsManager.updateCardBackgroundColor(cardBgColor.takeIf { it.isNotBlank() })
                        settingsManager.updateCardTitleColor(cardTitleColor.takeIf { it.isNotBlank() })
                        settingsManager.updateCardSubtitleColor(cardSubtitleColor.takeIf { it.isNotBlank() })
                        settingsManager.updateCardPadding(cardPadding)
                        onDismiss()
                    }
                ) {
                    Text("Сохранить")
                }
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
fun ColorDropdown(
    label: String,
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
            value = colorOptions.find { it.first == selectedColorHex }?.second
                ?: "Не выбран",
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            leadingIcon = {
                if (selectedColorHex.isNotBlank()) {
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .clip(CircleShape)
                            .background(colorFromHex(selectedColorHex))
                    )
                } else {
                    Icon(Icons.Default.ColorLens, contentDescription = null)
                }
            },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            colorOptions.forEach { (hex, displayName) ->
                DropdownMenuItem(
                    text = { Text(displayName) },
                    onClick = {
                        onColorSelected(hex)
                        expanded = false
                    },
                    leadingIcon = {
                        Box(
                            modifier = Modifier
                                .size(20.dp)
                                .clip(CircleShape)
                                .background(colorFromHex(hex))
                        )
                    }
                )
            }
        }
    }
}