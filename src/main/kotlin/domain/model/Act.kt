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

@Serializable
enum class ActCategory{
    SPORT,
    EDUCATION,
    ANOTHER

}

@Serializable
enum class ActType{
    ACTION,
    HABIT,
    VICE,        // пороки (курение, алкоголь, бессмысленные покупки)
}