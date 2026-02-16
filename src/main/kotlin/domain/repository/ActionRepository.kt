package app.majodesk.domain.repository

import app.majodesk.domain.model.Act


/**
 * описываем что класс должен уметь делать, чтобы быть объектом
 * который будет работать с активностями
 * */

interface ActRepository{
    fun createAct(act: Act)
    fun getActById(id: Long): Act?
}