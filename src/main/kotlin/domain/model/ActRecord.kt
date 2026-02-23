package app.majodesk.domain.model

import kotlinx.serialization.Serializable
import kotlinx.datetime.Instant
import kotlinx.datetime.Clock

@Serializable
data class ActRecord(
    val id: Long,
    val actId: Long,
    val startTime: Instant,
    val endTime: Instant? = null,
    val durationMinutes: Int? = null,
    val notes: String = "",
    val createdAt: Instant = Clock.System.now()
)