package app.majodesk.presentation.features.acts.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.majodesk.domain.model.ActCategory
import app.majodesk.domain.model.ActType
import app.majodesk.domain.model.Metric
import app.majodesk.presentation.features.acts.ActFormState

@Composable
fun ActForm(
    state: ActFormState,
    onNameChange: (String) -> Unit,
    onCategoryChange: (ActCategory) -> Unit,
    onTypeChange: (ActType) -> Unit,
    onRegularityChange: (Boolean) -> Unit,
    onMetricChange: (Metric) -> Unit,
    categories: List<ActCategory>,
    onAddCategoryClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    // ... тело остаётся тем же, просто меняем название параметра state
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        NameInput(
            value = state.name,
            onValueChange = onNameChange
        )
        CategoryDropdown(
            selectedCategory = state.category,
            onCategorySelected = onCategoryChange,
            categories = categories,
            onAddCategoryClick = onAddCategoryClick
                ?: {} // если null, передаём пустую лямбду, но внутри CategoryDropdown нужно скрыть пункт
            // Для этого модифицируем CategoryDropdown, чтобы он принимал onAddCategoryClick? и показывал пункт только если не null
        )

        TypeDropdown(
            selectedType = state.type,
            onTypeSelected = onTypeChange
        )

        RegularityCheckbox(
            checked = state.regularity,
            onCheckedChange = onRegularityChange
        )

        MetricInput(
            initialMetric = state.metric,
            onMetricChange = onMetricChange
        )
    }
}