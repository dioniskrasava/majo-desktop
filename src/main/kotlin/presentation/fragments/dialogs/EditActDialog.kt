package app.majodesk.presentation.fragments.dialogs

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
import app.majodesk.presentation.fragments.forms.ActForm
import app.majodesk.presentation.localization.stringResource
import app.majodesk.presentation.state.ActFormState

@Composable
fun EditActDialog(
    act: Act,
    categories: List<ActCategory>,
    onDismiss: () -> Unit,
    onConfirm: (Act) -> Unit
) {
    var formState by remember {
        mutableStateOf(
            ActFormState(
                name = act.name,
                category = act.category,
                type = act.type,
                regularity = act.regularity,
                metric = act.metric
            )
        )
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource("edit_activity")) },
        text = {
            ActForm(
                state = formState,
                onNameChange = { formState = formState.copy(name = it) },
                onCategoryChange = { formState = formState.copy(category = it) },
                onTypeChange = { formState = formState.copy(type = it) },
                onRegularityChange = { formState = formState.copy(regularity = it) },
                onMetricChange = { formState = formState.copy(metric = it) },
                categories = categories,
                onAddCategoryClick = null  // не показываем пункт добавления категории
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val updatedAct = act.copy(
                        name = formState.name,
                        category = formState.category,
                        type = formState.type,
                        regularity = formState.regularity,
                        metric = formState.metric
                    )
                    onConfirm(updatedAct)
                },
                enabled = formState.name.isNotBlank()
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