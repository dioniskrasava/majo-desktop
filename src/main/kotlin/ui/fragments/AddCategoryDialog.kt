package app.majodesk.ui.fragments

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import app.majodesk.domain.model.ActCategory

@Composable
fun AddCategoryDialog(
    onDismiss: () -> Unit,
    onConfirm: (ActCategory) -> Unit
) {
    var name by remember { mutableStateOf("") }
    // Для простоты используем фиксированные иконку и цвет,
    // в реальном приложении можно добавить выбор
    val defaultIcon = "category"
    val defaultColor = "#9E9E9E"

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Новая категория") },
        text = {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Название") },
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (name.isNotBlank()) {
                        onConfirm(ActCategory(name, defaultIcon, defaultColor))
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