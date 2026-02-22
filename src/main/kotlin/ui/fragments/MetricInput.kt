// ui/fragments/MetricInput.kt
package app.majodesk.ui.fragments

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.majodesk.domain.model.*
import app.majodesk.ui.localization.stringResource
import app.majodesk.domain.model.MetricType

@Composable
fun MetricInput(
    initialMetric: Metric?,
    onMetricChange: (Metric) -> Unit
) {
    // Определяем начальный тип метрики и её параметры
    val (initialType, initialPoints, initialDistanceUnit, initialWeightUnit, initialTimeUnit) = when (initialMetric) {
        is Metric.Count -> Quadruple(MetricType.COUNT, initialMetric.points, null, null, null)
        is Metric.Distance -> Quadruple(MetricType.DISTANCE, initialMetric.points, initialMetric.unit, null, null)
        is Metric.Weight -> Quadruple(MetricType.WEIGHT, initialMetric.points, null, initialMetric.unit, null)
        is Metric.Time -> Quadruple(MetricType.TIME, initialMetric.points, null, null, initialMetric.unit)
        null -> Quadruple(MetricType.COUNT, 1.0, null, null, null) // значения по умолчанию
    }

    var selectedType by remember { mutableStateOf(initialType) }
    var points by remember { mutableStateOf(initialPoints.toString()) }
    var selectedDistanceUnit by remember { mutableStateOf(initialDistanceUnit ?: DistanceUnit.KILOMETER) }
    var selectedWeightUnit by remember { mutableStateOf(initialWeightUnit ?: WeightUnit.KILOGRAM) }
    var selectedTimeUnit by remember { mutableStateOf(initialTimeUnit ?: TimeUnit.HOUR) }

    // Обновляем внешнее состояние при изменении любого параметра
    fun notifyChange() {
        val pointsValue = points.toDoubleOrNull() ?: 0.0
        val metric = when (selectedType) {
            MetricType.COUNT -> Metric.Count(pointsValue)
            MetricType.DISTANCE -> Metric.Distance(pointsValue, selectedDistanceUnit)
            MetricType.WEIGHT -> Metric.Weight(pointsValue, selectedWeightUnit)
            MetricType.TIME -> Metric.Time(pointsValue, selectedTimeUnit)
        }
        onMetricChange(metric)
    }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        // Выбор типа метрики
        TypeDropdown(
            selectedType = selectedType,
            onTypeSelected = {
                selectedType = it
                notifyChange()
            }
        )

        // Поле для ввода очков
        OutlinedTextField(
            value = points,
            onValueChange = {
                points = it
                notifyChange()
            },
            label = { Text("Очки за единицу") }, // добавить в локализацию
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = points.toDoubleOrNull() == null
        )

        // Дополнительные поля для единиц измерения
        when (selectedType) {
            MetricType.DISTANCE -> {
                UnitDropdown(
                    selectedUnit = selectedDistanceUnit,
                    onUnitSelected = {
                        selectedDistanceUnit = it
                        notifyChange()
                    },
                    units = DistanceUnit.values().toList(),
                    unitDisplay = { unit ->
                        when (unit) {
                            DistanceUnit.KILOMETER -> "км"
                            DistanceUnit.METER -> "м"
                        }
                    }
                )
            }
            MetricType.WEIGHT -> {
                UnitDropdown(
                    selectedUnit = selectedWeightUnit,
                    onUnitSelected = {
                        selectedWeightUnit = it
                        notifyChange()
                    },
                    units = WeightUnit.values().toList(),
                    unitDisplay = { unit ->
                        when (unit) {
                            WeightUnit.KILOGRAM -> "кг"
                            WeightUnit.TON -> "т"
                        }
                    }
                )
            }
            MetricType.TIME -> {
                UnitDropdown(
                    selectedUnit = selectedTimeUnit,
                    onUnitSelected = {
                        selectedTimeUnit = it
                        notifyChange()
                    },
                    units = TimeUnit.values().toList(),
                    unitDisplay = { unit ->
                        when (unit) {
                            TimeUnit.HOUR -> "ч"        // ЗАМЕНИТЬ НА  TimeUnit.HOUR -> stringResource("unit_h")
                            TimeUnit.MINUTE -> "мин"
                            TimeUnit.SECOND -> "сек"
                        }
                    }
                )
            }
            MetricType.COUNT -> { /* без единиц */ }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> UnitDropdown(
    selectedUnit: T,
    onUnitSelected: (T) -> Unit,
    units: List<T>,
    unitDisplay: (T) -> String
) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = unitDisplay(selectedUnit),
            onValueChange = {},
            readOnly = true,
            label = { Text("Единица измерения") }, // локализация
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            units.forEach { unit ->
                DropdownMenuItem(
                    text = { Text(unitDisplay(unit)) },
                    onClick = {
                        onUnitSelected(unit)
                        expanded = false
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TypeDropdown(
    selectedType: MetricType,
    onTypeSelected: (MetricType) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = when (selectedType) {
                MetricType.COUNT -> "Счётчик"
                MetricType.DISTANCE -> "Дистанция"
                MetricType.WEIGHT -> "Вес"
                MetricType.TIME -> "Время"
            },
            onValueChange = {},
            readOnly = true,
            label = { Text("Тип метрики") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            MetricType.values().forEach { type ->
                DropdownMenuItem(
                    text = {
                        Text(
                            when (type) {
                                MetricType.COUNT -> "Счётчик"
                                MetricType.DISTANCE -> "Дистанция"
                                MetricType.WEIGHT -> "Вес"
                                MetricType.TIME -> "Время"
                            }
                        )
                    },
                    onClick = {
                        onTypeSelected(type)
                        expanded = false
                    }
                )
            }
        }
    }
}

// Вспомогательный data class для возврата нескольких значений из when
private data class Quadruple<A, B, C, D, E>(
    val first: A, val second: B, val third: C, val fourth: D, val fifth: E
)