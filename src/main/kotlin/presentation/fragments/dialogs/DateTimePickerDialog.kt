package app.majodesk.presentation.fragments.dialogs

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.datetime.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateTimePickerDialog(
    initialDateTime: Instant,
    onDismiss: () -> Unit,
    onConfirm: (Instant) -> Unit
) {
    val localDateTime = initialDateTime.toLocalDateTime(TimeZone.currentSystemDefault())
    var year by remember { mutableStateOf(localDateTime.year) }
    var month by remember { mutableStateOf(localDateTime.monthNumber) }
    var day by remember { mutableStateOf(localDateTime.dayOfMonth) }
    var hour by remember { mutableStateOf(localDateTime.hour) }
    var minute by remember { mutableStateOf(localDateTime.minute) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Выберите дату и время") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    value = year.toString(),
                    onValueChange = { year = it.toIntOrNull() ?: year },
                    label = { Text("Год") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = month.toString(),
                    onValueChange = { month = it.toIntOrNull() ?: month },
                    label = { Text("Месяц") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = day.toString(),
                    onValueChange = { day = it.toIntOrNull() ?: day },
                    label = { Text("День") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = hour.toString(),
                    onValueChange = { hour = it.toIntOrNull() ?: hour },
                    label = { Text("Час (0-23)") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = minute.toString(),
                    onValueChange = { minute = it.toIntOrNull() ?: minute },
                    label = { Text("Минута (0-59)") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val newLocalDateTime = LocalDateTime(year, month, day, hour, minute)
                    val instant = newLocalDateTime.toInstant(TimeZone.currentSystemDefault())
                    onConfirm(instant)
                }
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена")
            }
        }
    )
}