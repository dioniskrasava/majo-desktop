package app.majodesk.data.matrix

interface MatrixRepository {
    fun loadConfig(): MatrixConfig?
    fun saveConfig(config: MatrixConfig)
}