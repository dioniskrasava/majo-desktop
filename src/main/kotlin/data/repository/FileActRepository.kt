package app.majodesk.data.repository

import app.majodesk.domain.model.Act
import app.majodesk.domain.model.ActCategory   // <-- добавить импорт
import app.majodesk.domain.repository.ActRepository
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

class FileActRepository(
    private val actsFile: File = File("acts.json"),
    private val categoriesFile: File = File("categories.json")
) : ActRepository {
    private val actsMap = mutableMapOf<Long, Act>()
    private var nextId: Long = 0
    private var categories: MutableList<ActCategory> = mutableListOf()

    init {
        loadActs()
        loadCategories()
    }

    override fun createAct(act: Act) {
        val newAct = act.copy(id = nextId)
        actsMap[nextId] = newAct
        nextId++
        saveActs()
        println("Активность сохранена: $newAct")
    }

    override fun getActById(id: Long): Act? = actsMap[id]

    override fun getAllActs(): List<Act> = actsMap.values.toList()

    override fun getAllCategories(): List<ActCategory> = categories.toList()

    override fun addCategory(category: ActCategory) {
        categories.add(category)
        saveCategories()
    }

    private fun loadActs() {
        if (!actsFile.exists()) return
        try {
            val content = actsFile.readText()
            val acts = Json.decodeFromString<List<Act>>(content)
            actsMap.clear()
            acts.forEach { actsMap[it.id] = it }
            nextId = (actsMap.keys.maxOrNull() ?: -1) + 1
            println("Загружено ${actsMap.size} активностей из файла.")
        } catch (e: Exception) {
            println("Ошибка загрузки из файла: ${e.message}")
        }
    }

    private fun saveActs() {
        try {
            val content = Json.encodeToString(actsMap.values.toList())
            actsFile.writeText(content)
        } catch (e: Exception) {
            println("Ошибка сохранения в файл: ${e.message}")
        }
    }

    private fun loadCategories() {
        if (!categoriesFile.exists()) {
            categories = mutableListOf(
                ActCategory.SPORT,
                ActCategory.EDUCATION,
                ActCategory.ANOTHER
            )
            saveCategories()
            return
        }
        try {
            val content = categoriesFile.readText()
            categories = Json.decodeFromString<List<ActCategory>>(content).toMutableList()
        } catch (e: Exception) {
            println("Ошибка загрузки категорий: ${e.message}")
            categories = mutableListOf(
                ActCategory.SPORT,
                ActCategory.EDUCATION,
                ActCategory.ANOTHER
            )
        }
    }

    private fun saveCategories() {
        try {
            val content = Json.encodeToString(categories)
            categoriesFile.writeText(content)
        } catch (e: Exception) {
            println("Ошибка сохранения категорий: ${e.message}")
        }
    }
}