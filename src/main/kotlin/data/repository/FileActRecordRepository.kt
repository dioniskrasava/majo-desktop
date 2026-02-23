package app.majodesk.data.repository

import app.majodesk.domain.model.ActRecord
import app.majodesk.domain.repository.ActRecordRepository
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

class FileActRecordRepository(
    private val recordsFile: File = File("records.json")
) : ActRecordRepository {

    private val recordsMap = mutableMapOf<Long, ActRecord>()
    private var nextId: Long = 0

    init {
        loadRecords()
    }

    override fun createRecord(record: ActRecord) {
        val newRecord = record.copy(id = nextId)
        recordsMap[nextId] = newRecord
        nextId++
        saveRecords()
        println("Запись активности сохранена: $newRecord")
    }

    override fun getRecordById(id: Long): ActRecord? = recordsMap[id]

    override fun getAllRecords(): List<ActRecord> = recordsMap.values.toList()

    override fun getRecordsByActId(actId: Long): List<ActRecord> =
        recordsMap.values.filter { it.actId == actId }

    override fun updateRecord(record: ActRecord) {
        if (recordsMap.containsKey(record.id)) {
            recordsMap[record.id] = record
            saveRecords()
            println("Запись активности обновлена: $record")
        } else {
            println("Ошибка: запись с id ${record.id} не найдена")
        }
    }

    override fun deleteRecord(id: Long) {
        if (recordsMap.remove(id) != null) {
            saveRecords()
            println("Запись с id $id удалена")
        } else {
            println("Ошибка: запись с id $id не найдена")
        }
    }

    private fun loadRecords() {
        if (!recordsFile.exists()) return
        try {
            val content = recordsFile.readText()
            val records = Json.decodeFromString<List<ActRecord>>(content)
            recordsMap.clear()
            records.forEach { recordsMap[it.id] = it }
            nextId = (recordsMap.keys.maxOrNull() ?: -1) + 1
            println("Загружено ${recordsMap.size} записей активностей.")
        } catch (e: Exception) {
            println("Ошибка загрузки записей из файла: ${e.message}")
        }
    }

    private fun saveRecords() {
        try {
            val content = Json.encodeToString(recordsMap.values.toList())
            recordsFile.writeText(content)
        } catch (e: Exception) {
            println("Ошибка сохранения записей в файл: ${e.message}")
        }
    }
}