package app.majodesk.domain.model

import kotlinx.serialization.Serializable
import kotlinx.datetime.Instant
import kotlinx.datetime.Clock

@Serializable
data class ActRecord(
    val id: Long,
    val actId: Long,
    val startTime: Instant,
    val value: Double,               // значение, интерпретируется согласно метрике активности
    val notes: String = "",
    val createdAt: Instant = Clock.System.now()
)