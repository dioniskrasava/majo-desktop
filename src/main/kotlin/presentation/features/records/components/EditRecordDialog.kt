package app.majodesk.presentation.features.records.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.majodesk.domain.model.Act
import app.majodesk.domain.model.ActRecord
import app.majodesk.presentation.core.DateTimePickerDialog
import app.majodesk.presentation.core.localization.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditRecordDialog(
    record: ActRecord,
    acts: List<Act>,
    onDismiss: () -> Unit,
    onConfirm: (ActRecord) -> Unit
) {
    val act = acts.find { it.id == record.actId }
    var selectedAct by remember { mutableStateOf(act ?: acts.firstOrNull()) }
    var value by remember { mutableStateOf(record.value.toString()) }
    var notes by remember { mutableStateOf(record.notes) }
    var startDateTime by remember { mutableStateOf(record.startTime) }
    var showDateTimePicker by remember { mutableStateOf(false) }

    val isValueValid = value.toDoubleOrNull() != null

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Редактировать запись") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                ActDropdown(
                    acts = acts,
                    selectedAct = selectedAct,
                    onActSelected = { selectedAct = it }
                )

                DateTimeDisplay(
                    dateTime = startDateTime,
                    onClick = { showDateTimePicker = true }
                )

                selectedAct?.let { act ->
                    ValueInput(
                        value = value,
                        onValueChange = { value = it },
                        metric = act.metric,
                        isError = !isValueValid && value.isNotBlank()
                    )
                }

                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text(stringResource("notes")) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (selectedAct != null && isValueValid) {
                        val updatedRecord = record.copy(
                            actId = selectedAct!!.id,
                            startTime = startDateTime,
                            value = value.toDouble(),
                            notes = notes
                        )
                        onConfirm(updatedRecord)
                    }
                },
                enabled = selectedAct != null && isValueValid
            ) {
                Text(stringResource("save"))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource("cancel"))
            }
        }
    )

    if (showDateTimePicker) {
        DateTimePickerDialog(
            initialDateTime = startDateTime,
            onDismiss = { showDateTimePicker = false },
            onConfirm = { newDateTime ->
                startDateTime = newDateTime
                showDateTimePicker = false
            }
        )
    }
}