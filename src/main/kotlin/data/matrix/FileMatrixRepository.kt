package app.majodesk.data.matrix

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File


/**
 * Реализация репозитория матрицы, использующая файл для хранения.
 *
 * @param configFile файл, в котором будет храниться конфигурация (по умолчанию "matrix.json")
 */
class FileMatrixRepository(
    private val configFile: File = File("matrix.json")
) : MatrixRepository {

    /**
     * Загружает конфигурацию из файла.
     * Если файл не существует или повреждён, возвращает null.
     */
    override fun loadConfig(): MatrixConfig? {
        if (!configFile.exists()) return null
        return try {
            val content = configFile.readText()
            Json.decodeFromString<MatrixConfig>(content)
        } catch (e: Exception) {
            println("Ошибка загрузки матрицы: ${e.message}")
            null
        }
    }

    /**
     * Сохраняет конфигурацию в файл в формате JSON.
     */
    override fun saveConfig(config: MatrixConfig) {
        try {
            val content = Json.encodeToString(config)
            configFile.writeText(content)
        } catch (e: Exception) {
            println("Ошибка сохранения матрицы: ${e.message}")
        }
    }

    /**
     * Удаляет файл с конфигурацией, если он существует.
     */
    override fun deleteConfig() {
        if (configFile.exists()) {
            configFile.delete()
        }
    }
}