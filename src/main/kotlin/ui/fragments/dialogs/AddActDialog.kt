package app.majodesk.ui.fragments.dialogs

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import app.majodesk.domain.model.Act
import app.majodesk.domain.model.ActCategory
import app.majodesk.domain.model.ActType
import app.majodesk.domain.model.Metric
import app.majodesk.ui.fragments.forms.ActForm
import app.majodesk.ui.localization.stringResource
import app.majodesk.ui.state.ActFormState

@Composable
fun AddActDialog(
    categories: List<ActCategory>,
    onDismiss: () -> Unit,
    onConfirm: (Act) -> Unit
) {
    var formState by remember {
        mutableStateOf(ActFormState()) // начальное состояние с пустыми полями
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource("add_activity")) },
        text = {
            ActForm(
                state = formState,
                onNameChange = { formState = formState.copy(name = it) },
                onCategoryChange = { formState = formState.copy(category = it) },
                onTypeChange = { formState = formState.copy(type = it) },
                onRegularityChange = { formState = formState.copy(regularity = it) },
                onMetricChange = { formState = formState.copy(metric = it) },
                categories = categories,
                onAddCategoryClick = null // или можно передать действие, но проще пока опустить
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val newAct = Act(
                        id = 0, // ID будет присвоен в репозитории
                        name = formState.name,
                        category = formState.category,
                        type = formState.type,
                        regularity = formState.regularity,
                        metric = formState.metric
                    )
                    onConfirm(newAct)
                },
                enabled = formState.name.isNotBlank()
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