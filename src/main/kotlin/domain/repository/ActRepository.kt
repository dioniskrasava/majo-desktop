package app.majodesk.domain.repository

import app.majodesk.domain.model.Act
import app.majodesk.domain.model.ActCategory


/**
 * Описываем что класс должен уметь делать, чтобы быть объектом
 * который будет работать с активностями
 * */

interface ActRepository {
    fun createAct(act: Act)
    fun getActById(id: Long): Act?
    fun getAllActs(): List<Act>
}