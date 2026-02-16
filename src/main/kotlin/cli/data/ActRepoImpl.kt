package app.majodesk.cli.data

import app.majodesk.domain.model.Act
import app.majodesk.domain.repository.ActRepository




class LocalActRepositoryImpl() : ActRepository{

    val actsMap = mutableMapOf<Long, Act>()
    var lastId : Long = 0

    /**
     * Игнорирует id переданного объекта и меняет его по типу автоинкримента
     * */
    override fun createAct(act: Act) {
        val newAct = act.copy(id = lastId)
        actsMap[lastId] = newAct
        lastId += 1
        println("ЛОКАЛЬНО СОЗДАНА АКТИВНОСТЬ")
    }

    override fun getActById(id: Long): Act? {
        println("АКТИВНОСТЬ С ID :")
        return actsMap.get(id)

    }

    fun getAllActs(){
        println("СПИСОК ВСЕХ АКТИВНОСТЕЙ: ")
    }
}