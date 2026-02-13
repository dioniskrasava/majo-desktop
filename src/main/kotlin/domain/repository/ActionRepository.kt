package app.majodesk.domain.repository

import app.majodesk.domain.model.Action


/**
 * описываем что класс должен уметь делать, чтобы быть объектом
 * который будет работать с активностями
 * */

interface ActionRepository{
    fun createAction()
    fun getActionById(id: Int): Action?
}