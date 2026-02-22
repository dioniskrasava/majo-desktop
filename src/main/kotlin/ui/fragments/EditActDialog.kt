package app.majodesk.ui.fragments

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.majodesk.domain.model.Act
import app.majodesk.domain.model.ActCategory
import app.majodesk.ui.localization.stringResource

@Composable
fun EditActDialog(
    act: Act,
    categories: List<ActCategory>,
    onDismiss: () -> Unit,
    onConfirm: (Act) -> Unit
) {
    var name by remember { mutableStateOf(act.name) }
    var selectedCategory by remember { mutableStateOf(act.category) }
    var selectedType by remember { mutableStateOf(act.type) }
    var isRegular by remember { mutableStateOf(act.regularity) }
    var metric by remember { mutableStateOf(act.metric) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource("edit_activity")) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(stringResource("activity_name")) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                CategoryDropdown(
                    selectedCategory = selectedCategory,
                    onCategorySelected = { selectedCategory = it },
                    categories = categories,
                    onAddCategoryClick = { /* не показываем добавление в диалоге редактирования */ }
                )
                TypeDropdown(
                    selectedType = selectedType,
                    onTypeSelected = { selectedType = it }
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = isRegular, onCheckedChange = { isRegular = it })
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(stringResource("regular"))
                }
                MetricInput(
                    initialMetric = metric,
                    onMetricChange = { metric = it }
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val updatedAct = act.copy(
                        name = name,
                        category = selectedCategory,
                        type = selectedType,
                        regularity = isRegular,
                        metric = metric
                    )
                    onConfirm(updatedAct)
                },
                enabled = name.isNotBlank()
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
}