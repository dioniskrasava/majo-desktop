package app.majodesk.data.settings

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

class FileSettingsRepository(
    private val settingsFile: File = File("settings.json")
) : SettingsRepository {

    override fun load(): AppSettings {
        if (!settingsFile.exists()) return AppSettings()
        return try {
            val content = settingsFile.readText()
            Json.decodeFromString<AppSettings>(content)
        } catch (e: Exception) {
            println("Ошибка загрузки настроек: ${e.message}")
            AppSettings()
        }
    }

    override fun save(settings: AppSettings) {
        try {
            val content = Json.encodeToString(settings)
            settingsFile.writeText(content)
        } catch (e: Exception) {
            println("Ошибка сохранения настроек: ${e.message}")
        }
    }
}