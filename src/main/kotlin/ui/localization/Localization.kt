package app.majodesk.ui.localization

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.Composable

enum class Lang {
    RU, EN
}

class LocalizationManager {
    var currentLang by mutableStateOf(Lang.RU)
        private set

    fun setLanguage(lang: Lang) {
        currentLang = lang
    }

    fun getString(key: String): String {
        return when (currentLang) {
            Lang.RU -> ru[key]
            Lang.EN -> en[key]
        } ?: "??$key??"
    }
}

val LocalLocalizationManager = compositionLocalOf { LocalizationManager() }

@Composable
fun stringResource(key: String): String {
    return LocalLocalizationManager.current.getString(key)
}

// Ресурсы для русского и английского языков
private val ru = mapOf(
    "app_title" to "Majo Desktop",
    "activities" to "Активности",
    "statistics" to "Статистика",
    "settings" to "Настройки",
    "theme_dark" to "Тёмная тема",
    "add_activity" to "Добавить активность",
    "activity_name" to "Название активности",
    "category" to "Категория",
    "type" to "Тип",
    "regular" to "Регулярная активность",
    "regular(short)" to "Регулярная",
    "add_category" to "+ Добавить категорию",
    "new_category" to "Новая категория",
    "name" to "Название",
    "icon" to "Иконка",
    "color" to "Цвет",
    "cancel" to "Отмена",
    "add" to "Добавить",
    "no_activities" to "Пока нет активностей.\nДобавьте первую!",
    "category_label" to "Категория:",
    "type_action" to "Действие",
    "type_habit" to "Привычка",
    "type_vice" to "Порок",
    "language" to "Язык",
    "russian" to "Русский",
    "english" to "Английский",
    "icon_fitness_center" to "Спорт",
    "icon_menu_book" to "Образование",
    "icon_category" to "Категория",
    "icon_sports_tennis" to "Теннис",
    "color_green" to "Зелёный",
    "color_blue" to "Синий",
    "color_grey" to "Серый",
    "color_red" to "Красный",
    "color_yellow" to "Жёлтый",
    "color_purple" to "Фиолетовый",
    "save" to "Сохранить",
    "edit_activity" to "Редактировать активность",
    "metric_type" to "Тип метрики",
    "metric_points" to "Очки за единицу",
    "metric_unit" to "Единица измерения",
    "metric_count" to "Счётчик",
    "metric_distance" to "Дистанция",
    "metric_weight" to "Вес",
    "metric_time" to "Время",
    "unit_km" to "километр",
    "unit_m" to "метр",
    "unit_kg" to "килограмм",
    "unit_t" to "тонн",
    "unit_h" to "часов",
    "unit_min" to "минут"
)

private val en = mapOf(
    "app_title" to "Majo Desktop",
    "activities" to "Activities",
    "statistics" to "Statistics",
    "settings" to "Settings",
    "theme_dark" to "Dark theme",
    "add_activity" to "Add activity",
    "activity_name" to "Activity name",
    "category" to "Category",
    "type" to "Type",
    "regular" to "Regular activity",
    "regular(short)" to "Regular",
    "add_category" to "+ Add category",
    "new_category" to "New category",
    "name" to "Name",
    "icon" to "Icon",
    "color" to "Color",
    "cancel" to "Cancel",
    "add" to "Add",
    "no_activities" to "No activities yet.\nAdd the first one!",
    "category_label" to "Category:",
    "type_action" to "Action",
    "type_habit" to "Habit",
    "type_vice" to "Vice",
    "language" to "Language",
    "russian" to "Russian",
    "english" to "English",
    "icon_fitness_center" to "Sport",
    "icon_menu_book" to "Education",
    "icon_category" to "Category",
    "icon_sports_tennis" to "Tennis",
    "color_green" to "Green",
    "color_blue" to "Blue",
    "color_grey" to "Grey",
    "color_red" to "Red",
    "color_yellow" to "Yellow",
    "color_purple" to "Purple",
    "save" to "Save",
    "edit_activity" to "Edit activity",
    "metric_type" to "Type of metric",
    "metric_points" to "Points per unit",
    "metric_unit" to "Unit of measurement",
    "metric_count" to "Count",
    "metric_distance" to "Distance",
    "metric_weight" to "Weight",
    "metric_time" to "Time",
    "unit_km" to "kilometer",
    "unit_m" to "meter",
    "unit_kg" to "kilogram",
    "unit_t" to "ton",
    "unit_h" to "hour",
    "unit_min" to "minutes"
)