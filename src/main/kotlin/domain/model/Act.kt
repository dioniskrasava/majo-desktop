package app.majodesk.domain.model

import kotlinx.serialization.Serializable

/**
 *
 * @param id Уникальный идентификатор.
 * @param name Название для пользователя.
 * @param category Категория активности (образование, спорт). Например [ActCategory.EDUCATION], [ActCategory.SPORT].
 * @param type Тип активности (действие, привычка, порок).
 * @param regularity Флаг регулярности.
 * */
@Serializable
data class Act(
    val id: Long,
    val name: String,
    val category: ActCategory = ActCategory.ANOTHER,
    val type: ActType = ActType.ACTION,
    val regularity: Boolean = true,
    )

// Стандартные категории можно определить как константы в companion object
@Serializable
data class ActCategory(
    val name: String,
    val iconName: String = "default",   // например, "fitness_center", "menu_book", "category"
    val colorHex: String = "#9E9E9E"    // цвет в формате #RRGGBB или #AARRGGBB
) {
    companion object {
        val SPORT = ActCategory("Спорт", "fitness_center", "#4CAF50")
        val EDUCATION = ActCategory("Образование", "menu_book", "#2196F3")
        val ANOTHER = ActCategory("Другое", "category", "#9E9E9E")
    }
}

@Serializable
enum class ActType{
    ACTION,
    HABIT,
    VICE,        // пороки (курение, алкоголь, бессмысленные покупки)
}