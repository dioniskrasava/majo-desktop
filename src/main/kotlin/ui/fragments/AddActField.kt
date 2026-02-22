package app.majodesk.ui.fragments

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import app.majodesk.domain.model.ActCategory
import app.majodesk.domain.model.ActType
import app.majodesk.domain.model.Metric
import app.majodesk.ui.colorFromHex
import app.majodesk.ui.iconFromName
import app.majodesk.ui.localization.stringResource

/**
 * Карточка для ввода данных новой активности.
 * @param onAddClick функция, которая будет вызвана при нажатии кнопки "Добавить"
 *                   и получит введённые данные: название, категорию, тип и регулярность.
 */
@Composable
fun AddActCard(
    categories: List<ActCategory>,
    onAddCategoryClick: () -> Unit,
    onAddClick: (name: String, category: ActCategory, type: ActType, regularity: Boolean, metric: Metric) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(ActCategory.ANOTHER) }
    var selectedType by remember { mutableStateOf(ActType.ACTION) }
    var isRegular by remember { mutableStateOf(true) }
    var metric by remember { mutableStateOf<Metric>(Metric.Count(1.0)) } // начальное значение

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        NameInput(value = name, onValueChange = { name = it })
        CategoryDropdown(
            selectedCategory = selectedCategory,
            onCategorySelected = { selectedCategory = it },
            categories = categories,
            onAddCategoryClick = onAddCategoryClick
        )
        TypeDropdown(
            selectedType = selectedType,
            onTypeSelected = { selectedType = it }
        )
        RegularityCheckbox(
            checked = isRegular,
            onCheckedChange = { isRegular = it }
        )
        MetricInput(
            initialMetric = metric,
            onMetricChange = { metric = it }
        )
        Spacer(modifier = Modifier.height(8.dp))
        AddButton(
            enabled = name.isNotBlank(),
            onClick = {
                onAddClick(name, selectedCategory, selectedType, isRegular, metric)
            }
        )
    }
}

@Composable
fun NameInput(value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(stringResource("activity_name")) },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryDropdown(
    selectedCategory: ActCategory,
    onCategorySelected: (ActCategory) -> Unit,
    categories: List<ActCategory>,
    onAddCategoryClick: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selectedCategory.name,
            onValueChange = {},
            readOnly = true,
            label = { Text(stringResource("category")) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            categories.forEach { category ->
                DropdownMenuItem(
                    text = { Text(category.name) },
                    onClick = {
                        onCategorySelected(category)
                        expanded = false
                    },
                    leadingIcon = {
                        Icon(
                            iconFromName(category.iconName),
                            contentDescription = null,
                            tint = colorFromHex(category.colorHex)
                        )
                    }
                )
            }
            Divider()
            DropdownMenuItem(
                text = { Text(stringResource("add_category")) },
                onClick = {
                    expanded = false
                    onAddCategoryClick()
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TypeDropdown(
    selectedType: ActType,
    onTypeSelected: (ActType) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = stringResource(
                when (selectedType) {
                    ActType.ACTION -> "type_action"
                    ActType.HABIT -> "type_habit"
                    ActType.VICE -> "type_vice"
                }
            ),
            onValueChange = {},
            readOnly = true,
            label = { Text(stringResource("type")) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            ActType.values().forEach { type ->
                DropdownMenuItem(
                    text = { Text(
                        stringResource(
                            when (type) {
                                ActType.ACTION -> "type_action"
                                ActType.HABIT -> "type_habit"
                                ActType.VICE -> "type_vice"
                            }
                        )
                    ) },
                    onClick = {
                        onTypeSelected(type)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun RegularityCheckbox(checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = stringResource("regular"),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun AddButton(enabled: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(stringResource("add_activity"))
    }
}
