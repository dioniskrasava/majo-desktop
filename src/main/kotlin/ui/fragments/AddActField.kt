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
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.majodesk.domain.model.ActCategory
import app.majodesk.domain.model.ActType

/**
 * Карточка для ввода данных новой активности.
 * @param onAddClick колбэк, вызываемый при нажатии кнопки "Добавить".
 *                   Возвращает введённые данные: название, категорию, тип, регулярность.
 */
@Composable
fun AddActCard(
    onAddClick: (name: String, category: ActCategory, type: ActType, regularity: Boolean) -> Unit
) {
    // Состояния полей
    var name by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(ActCategory.ANOTHER) }
    var selectedType by remember { mutableStateOf(ActType.ACTION) }
    var isRegular by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Поле ввода названия
        NameInput(
            value = name,
            onValueChange = { name = it }
        )

        // Выпадающий список категорий
        CategoryDropdown(
            selectedCategory = selectedCategory,
            onCategorySelected = { selectedCategory = it }
        )

        // Выпадающий список типов
        TypeDropdown(
            selectedType = selectedType,
            onTypeSelected = { selectedType = it }
        )

        // Чекбокс регулярности
        RegularityCheckbox(
            checked = isRegular,
            onCheckedChange = { isRegular = it }
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Кнопка добавления
        AddButton(
            enabled = name.isNotBlank(),
            onClick = {
                onAddClick(name, selectedCategory, selectedType, isRegular)
                // Опционально: сброс полей после добавления
                // name = ""
                // selectedCategory = ActCategory.ANOTHER
                // selectedType = ActType.ACTION
                // isRegular = true
            }
        )
    }
}

/**
 * Поле ввода названия активности.
 */
@Composable
fun NameInput(
    value: String,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text("Название активности") },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        isError = value.isBlank() // показываем ошибку, если поле пустое
    )
}

/**
 * Выпадающий список для выбора категории активности.
 */
@Composable
fun CategoryDropdown(
    selectedCategory: ActCategory,
    onCategorySelected: (ActCategory) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        Button(
            onClick = { expanded = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(selectedCategory.displayName)
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            ActCategory.values().forEach { category ->
                DropdownMenuItem(
                    text = { Text(category.displayName) },
                    onClick = {
                        onCategorySelected(category)
                        expanded = false
                    }
                )
            }
        }
    }
}

/**
 * Выпадающий список для выбора типа активности.
 */
@Composable
fun TypeDropdown(
    selectedType: ActType,
    onTypeSelected: (ActType) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth()) {
        Button(
            onClick = { expanded = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(selectedType.displayName)
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            ActType.values().forEach { type ->
                DropdownMenuItem(
                    text = { Text(type.displayName) },
                    onClick = {
                        onTypeSelected(type)
                        expanded = false
                    }
                )
            }
        }
    }
}

/**
 * Чекбокс для указания регулярности активности.
 */
@Composable
fun RegularityCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
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
            text = "Регулярная активность",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

/**
 * Кнопка добавления активности.
 * @param enabled активна ли кнопка (например, если название не пустое).
 */
@Composable
fun AddButton(
    enabled: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("Добавить активность")
    }
}

/**
 * Расширения для получения читаемого имени категории.
 */
private val ActCategory.displayName: String
    get() = when (this) {
        ActCategory.SPORT -> "Спорт"
        ActCategory.EDUCATION -> "Образование"
        //ActCategory.WORK -> "Работа"
        //ActCategory.STUDY -> "Учёба"
        //ActCategory.HOBBY -> "Хобби"
        //ActCategory.HEALTH -> "Здоровье"
        ActCategory.ANOTHER -> "Другое"
    }

/**
 * Расширения для получения читаемого имени типа активности.
 */
private val ActType.displayName: String
    get() = when (this) {
        ActType.ACTION -> "Действие"
        //ActType.PROJECT -> "Проект"
        ActType.HABIT -> "Привычка"
        ActType.VICE -> "Пороки"
    }