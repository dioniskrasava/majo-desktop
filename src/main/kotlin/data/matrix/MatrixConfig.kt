package app.majodesk.data.matrix

import kotlinx.serialization.Serializable

@Serializable
data class MatrixConfig(
    val orderedActivityIds: List<Long> = emptyList()
)