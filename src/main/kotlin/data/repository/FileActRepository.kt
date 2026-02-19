package app.majodesk.data.repository

import app.majodesk.domain.model.Act
import app.majodesk.domain.repository.ActRepository
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

/**
 * Класс, описывающий объект, который сможет сохранять активности в файл json
 *
 * */
class FileActRepository(
    private val jsonFile: File = File("acts.json")
) : ActRepository {

    private val actsMap = mutableMapOf<Long, Act>()
    private var nextId: Long = 0

    init {
        loadFromFile()
    }

    override fun createAct(act: Act) {
        val newAct = act.copy(id = nextId)
        actsMap[nextId] = newAct
        nextId++
        saveToFile()
        println("Активность сохранена: ${newAct}")
    }

    override fun getActById(id: Long): Act? {
        return actsMap[id]
    }

    override fun getAllActs(): List<Act> = actsMap.values.toList()

    // exists = существует
    private fun loadFromFile(){
        if (!jsonFile.exists()) return
        try {
            val content = jsonFile.readText()
            val acts = Json.decodeFromString<List<Act>>(content)
            actsMap.clear()
            acts.forEach { actsMap[it.id] = it }
            nextId = (actsMap.keys.maxOrNull() ?: -1) + 1
            println("Загружено ${actsMap.size} активностей из файла.")

        } catch (e: Exception) {
            println("Ошибка загрузки из файла: ${e.message}")
        }
    }


    private fun saveToFile(){
        try {
            val content = Json.encodeToString(actsMap.values.toList())
            jsonFile.writeText(content)
        } catch (e: Exception){
            println("Ошибка сохранения в файл : ${e.message}")
        }
    }

}