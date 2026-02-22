package app.majodesk.domain.model

import kotlinx.serialization.Serializable

/* Типы метрик которые может выбрать пользователь */
enum class MetricType {
    COUNT, DISTANCE, WEIGHT, TIME
}

@Serializable
sealed class Metric {
    /** Количество очков за одну единицу измерения (или за факт выполнения для бинарной метрики) */
    abstract val points: Double

    /** Дистанционная метрика (бег, вело) */
    @Serializable
    data class Distance(
        override val points: Double,
        val unit: DistanceUnit
    ) : Metric()

    /** Весовая метрика (штанга) */
    @Serializable
    data class Weight(
        override val points: Double,
        val unit: WeightUnit
    ) : Metric()

    /** Временная метрика (медитация) */
    @Serializable
    data class Time(
        override val points: Double,
        val unit: TimeUnit
    ) : Metric()

    /** Бинарная / счетная метрика (отжимания, курение) – "сделал/не сделал" или подсчёт повторений */
    @Serializable
    data class Count(
        override val points: Double
    ) : Metric()
}