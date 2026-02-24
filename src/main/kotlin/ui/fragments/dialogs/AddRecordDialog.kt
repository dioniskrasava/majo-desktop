package app.majodesk.ui.fragments.dialogs

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.majodesk.domain.model.Act
import app.majodesk.domain.model.ActRecord
import app.majodesk.domain.model.Metric
import app.majodesk.domain.model.DistanceUnit
import app.majodesk.domain.model.WeightUnit
import app.majodesk.domain.model.TimeUnit
import app.majodesk.ui.localization.stringResource
import kotlinx.datetime.*
import kotlinx.datetime.TimeZone

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddRecordDialog(
    acts: List<Act>,
    onDismiss: () -> Unit,
    onConfirm: (ActRecord) -> Unit
) {
    var selectedAct by remember { mutableStateOf(acts.firstOrNull()) }
    var value by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var startDateTime by remember { mutableStateOf(Clock.System.now()) }
    var showDateTimePicker by remember { mutableStateOf(false) }

    val isValueValid = value.toDoubleOrNull() != null

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource("add_record")) },
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
                        val record = ActRecord(
                            id = 0,
                            actId = selectedAct!!.id,
                            startTime = startDateTime,
                            value = value.toDouble(),
                            notes = notes
                        )
                        onConfirm(record)
                    }
                },
                enabled = selectedAct != null && isValueValid
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
            label = { Text(stringResource("activity")) },
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

@Composable
fun DateTimeDisplay(
    dateTime: Instant,
    onClick: () -> Unit
) {
    val localDateTime = dateTime.toLocalDateTime(TimeZone.currentSystemDefault())
    val formatted = buildString {
        append(localDateTime.date.dayOfMonth.toString().padStart(2, '0'))
        append('.')
        append(localDateTime.date.monthNumber.toString().padStart(2, '0'))
        append('.')
        append(localDateTime.date.year)
        append(' ')
        append(localDateTime.time.hour.toString().padStart(2, '0'))
        append(':')
        append(localDateTime.time.minute.toString().padStart(2, '0'))
    }
    OutlinedTextField(
        value = formatted,
        onValueChange = {},
        label = { Text(stringResource("start_time")) },
        readOnly = true,
        modifier = Modifier.fillMaxWidth(),
        trailingIcon = {
            IconButton(onClick = onClick) {
                Icon(Icons.Default.Edit, contentDescription = "Изменить дату/время")
            }
        }
    )
}

@Composable
fun ValueInput(
    value: String,
    onValueChange: (String) -> Unit,
    metric: Metric,
    isError: Boolean
) {
    val label = when (metric) {
        is Metric.Count -> "${stringResource("metric_count")} (${stringResource("unit_pcs")})"
        is Metric.Distance -> "${stringResource("metric_distance")} (${unitString(metric.unit)})"
        is Metric.Weight -> "${stringResource("metric_weight")} (${unitString(metric.unit)})"
        is Metric.Time -> "${stringResource("metric_time")} (${unitString(metric.unit)})"
    }
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        isError = isError
    )
}

private fun unitString(unit: DistanceUnit): String = when (unit) {
    DistanceUnit.KILOMETER -> "км"
    DistanceUnit.METER -> "м"
}
private fun unitString(unit: WeightUnit): String = when (unit) {
    WeightUnit.KILOGRAM -> "кг"
    WeightUnit.TON -> "т"
}
private fun unitString(unit: TimeUnit): String = when (unit) {
    TimeUnit.HOUR -> "ч"
    TimeUnit.MINUTE -> "мин"
    TimeUnit.SECOND -> "сек"
}