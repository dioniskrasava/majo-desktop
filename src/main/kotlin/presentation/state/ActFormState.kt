package app.majodesk.presentation.state


import app.majodesk.domain.model.ActCategory
import app.majodesk.domain.model.ActType
import app.majodesk.domain.model.Metric

data class ActFormState(
    val name: String = "",
    val category: ActCategory = ActCategory.ANOTHER,
    val type: ActType = ActType.ACTION,
    val regularity: Boolean = true,
    val metric: Metric = Metric.Count(1.0)
)