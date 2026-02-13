package app.majodesk.domain.model

data class Action(
    val id: Int,
    val name: String,
    val category: String,    //  enum?
    val unit: String,        //  переписать на enum class
)
