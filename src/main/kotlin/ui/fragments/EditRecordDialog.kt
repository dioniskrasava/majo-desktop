// file: ui/fragments/EditRecordDialog.kt
package app.majodesk.ui.fragments

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.majodesk.domain.model.Act
import app.majodesk.domain.model.ActRecord
import app.majodesk.ui.localization.stringResource
import kotlinx.datetime.*
import kotlinx.datetime.TimeZone
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditRecordDialog(
    record: ActRecord,
    acts: List<Act>,
    onDismiss: () -> Unit,
    onConfirm: (ActRecord) -> Unit
) {
    // Состояния для редактируемых полей
    var selectedAct by remember { mutableStateOf(acts.find { it.id == record.actId } ?: acts.firstOrNull()) }
    var startDateTime by remember { mutableStateOf(record.startTime) }
    var durationMinutes by remember { mutableStateOf(record.durationMinutes?.toString() ?: "") }
    var notes by remember { mutableStateOf(record.notes) }

    // Состояния для пикеров
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    // Для пикера даты преобразуем Instant в LocalDate
    val localDate = startDateTime.toLocalDateTime(TimeZone.currentSystemDefault()).date
    val localTime = startDateTime.toLocalDateTime(TimeZone.currentSystemDefault()).time

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Редактировать запись") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                // Выбор активности
                ActDropdown(
                    acts = acts,
                    selectedAct = selectedAct,
                    onActSelected = { selectedAct = it }
                )

                // Выбор даты и времени
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = formatDate(localDate),
                        onValueChange = {},
                        label = { Text("Дата") },
                        readOnly = true,
                        modifier = Modifier.weight(1f),
                        trailingIcon = {
                            IconButton(onClick = { showDatePicker = true }) {
                                Icon(Icons.Default.Edit, null)
                            }
                        }
                    )
                    OutlinedTextField(
                        value = formatTime(localTime),
                        onValueChange = {},
                        label = { Text("Время") },
                        readOnly = true,
                        modifier = Modifier.weight(1f),
                        trailingIcon = {
                            IconButton(onClick = { showTimePicker = true }) {
                                Icon(Icons.Default.Edit, null)
                            }
                        }
                    )
                }

                // Длительность
                OutlinedTextField(
                    value = durationMinutes,
                    onValueChange = { durationMinutes = it },
                    label = { Text("Длительность (мин)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                // Заметки
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Заметки") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (selectedAct != null) {
                        val updatedRecord = record.copy(
                            actId = selectedAct!!.id,
                            startTime = startDateTime,
                            durationMinutes = durationMinutes.toIntOrNull(),
                            notes = notes
                        )
                        onConfirm(updatedRecord)
                    }
                },
                enabled = selectedAct != null
            ) {
                Text("Сохранить")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена")
            }
        }
    )

    // Диалог выбора даты
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("ОК") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Отмена") }
            }
        ) {
            // В Compose Desktop DatePicker может быть нестабилен, поэтому для простоты используем кастомный выбор
            // Вместо этого можно реализовать выпадающие списки или текстовые поля.
            // Здесь для краткости оставим заглушку — фактически нужно реализовать выбор даты.
            // Рекомендуется использовать ExposedDropdownMenu для года, месяца, дня.
            Text("Здесь должен быть DatePicker")
        }
    }
}

// Вспомогательные функции форматирования
private fun formatDate(date: LocalDate): String {
    return "${date.dayOfMonth.toString().padStart(2, '0')}.${date.monthNumber.toString().padStart(2, '0')}.${date.year}"
}

private fun formatTime(time: LocalTime): String {
    return "${time.hour.toString().padStart(2, '0')}:${time.minute.toString().padStart(2, '0')}"
}