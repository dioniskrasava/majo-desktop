package app.majodesk.ui.fragments

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.majodesk.domain.model.Act
import app.majodesk.domain.model.ActRecord
import app.majodesk.ui.localization.stringResource
import kotlinx.datetime.Instant
import kotlinx.datetime.Clock

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddRecordDialog(
    acts: List<Act>,
    onDismiss: () -> Unit,
    onConfirm: (ActRecord) -> Unit
) {
    var selectedAct by remember { mutableStateOf(acts.firstOrNull()) }
    var startTime by remember { mutableStateOf(Clock.System.now()) }
    var durationMinutes by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Добавить запись") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                // Выбор активности
                ActDropdown(
                    acts = acts,
                    selectedAct = selectedAct,
                    onActSelected = { selectedAct = it }
                )

                // Поля времени можно доработать под выбор даты/времени, но для простоты используем текстовые поля
                OutlinedTextField(
                    value = startTime.toString(), // в реальном приложении нужен DatePicker
                    onValueChange = {},
                    label = { Text("Время начала") },
                    enabled = false, // пока не реализовано
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = durationMinutes,
                    onValueChange = { durationMinutes = it },
                    label = { Text("Длительность (мин)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

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
                        val record = ActRecord(
                            id = 0,
                            actId = selectedAct!!.id,
                            startTime = startTime,
                            durationMinutes = durationMinutes.toIntOrNull(),
                            notes = notes
                        )
                        onConfirm(record)
                    }
                },
                enabled = selectedAct != null
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
fun ActDropdown(
    acts: List<Act>,
    selectedAct: Act?,
    onActSelected: (Act) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selectedAct?.name ?: "",
            onValueChange = {},
            readOnly = true,
            label = { Text("Активность") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            acts.forEach { act ->
                DropdownMenuItem(
                    text = { Text(act.name) },
                    onClick = {
                        onActSelected(act)
                        expanded = false
                    }
                )
            }
        }
    }
}