package app.majodesk.domain.model

/**
 *
 * @param id Уникальный идентификатор.
 * @param name Название для пользователя.
 * @param category Категория активности (образование, спорт). Например [ActCategory.EDUCATION], [ActCategory.SPORT].
 * @param type Тип активности (действие, привычка, порок).
 * @param regularity Флаг регулярности.
 * */
data class Act(
    val id: Long,
    val name: String,
    val category: ActCategory = ActCategory.ANOTHER,
    val type: ActType = ActType.ACTION,
    val regularity: Boolean = true,
    )

enum class ActCategory{
    SPORT,
    EDUCATION,
    ANOTHER

}

enum class ActType{
    ACTION,
    HABIT,
    VICE,        // пороки (курение, алкоголь, бессмысленные покупки)
}