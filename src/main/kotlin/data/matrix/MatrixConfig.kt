package app.majodesk.data.matrix

import kotlinx.serialization.Serializable


/**
 * Конфигурация матрицы активностей.
 * Хранит список идентификаторов активностей в том порядке,
 * в котором они должны отображаться в матрице.
 *
 * @property orderedActivityIds список id активностей (Long) в нужном порядке.
 * */

@Serializable
data class MatrixConfig(
    val orderedActivityIds: List<Long> = emptyList()
)