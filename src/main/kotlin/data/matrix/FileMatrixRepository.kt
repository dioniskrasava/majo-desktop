package app.majodesk.data.matrix

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

class FileMatrixRepository(
    private val configFile: File = File("matrix.json")
) : MatrixRepository {
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

    override fun saveConfig(config: MatrixConfig) {
        try {
            val content = Json.encodeToString(config)
            configFile.writeText(content)
        } catch (e: Exception) {
            println("Ошибка сохранения матрицы: ${e.message}")
        }
    }

    override fun deleteConfig() {
        if (configFile.exists()) {
            configFile.delete()
        }
    }
}