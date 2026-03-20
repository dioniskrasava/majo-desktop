package app.majodesk.presentation.features.records.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
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
import app.majodesk.presentation.core.Period
import app.majodesk.presentation.core.localization.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordsControls(
    selectedPeriod: Period,
    onPeriodChange: (Period) -> Unit,
    itemsPerPage: Int,
    onItemsPerPageChange: (Int) -> Unit,
    currentPage: Int,
    totalPages: Int,
    onPageChange: (Int) -> Unit
) {
    val periodOptions = Period.values().toList()
    val itemsPerPageOptions = listOf(30, 50, 100)

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Строка с выбором периода и количества
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Выбор периода
            var periodExpanded by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = periodExpanded,
                onExpandedChange = { periodExpanded = !periodExpanded }
            ) {
                OutlinedTextField(
                    value = stringResource(selectedPeriod.stringKey()),
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = periodExpanded) },
                    modifier = Modifier.menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = periodExpanded,
                    onDismissRequest = { periodExpanded = false }
                ) {
                    periodOptions.forEach { period ->
                        DropdownMenuItem(
                            text = { Text(stringResource(period.stringKey())) },
                            onClick = {
                                onPeriodChange(period)
                                periodExpanded = false
                            }
                        )
                    }
                }
            }

            // Выбор количества записей на странице
            var itemsExpanded by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = itemsExpanded,
                onExpandedChange = { itemsExpanded = !itemsExpanded }
            ) {
                OutlinedTextField(
                    value = itemsPerPage.toString(),
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = itemsExpanded) },
                    modifier = Modifier.menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = itemsExpanded,
                    onDismissRequest = { itemsExpanded = false }
                ) {
                    itemsPerPageOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text("$option") },
                            onClick = {
                                onItemsPerPageChange(option)
                                itemsExpanded = false
                            }
                        )
                    }
                }
            }
        }

        // Навигация по страницам
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(
                onClick = { onPageChange(currentPage - 1) },
                enabled = currentPage > 0
            ) {
                Text(stringResource("previous"))
            }
            Text(
                text = "${stringResource("page")} ${currentPage + 1} ${stringResource("of")} $totalPages",
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            TextButton(
                onClick = { onPageChange(currentPage + 1) },
                enabled = currentPage < totalPages - 1
            ) {
                Text(stringResource("next"))
            }
        }
    }
}