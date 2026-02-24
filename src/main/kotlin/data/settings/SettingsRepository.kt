package app.majodesk.data.settings


interface SettingsRepository {
    fun load(): AppSettings
    fun save(settings: AppSettings)
}