package app.majodesk.domain.repository

import app.majodesk.domain.model.ActCategory

interface CategoryRepository {
    fun getAllCategories(): List<ActCategory>
    fun addCategory(category: ActCategory)
}