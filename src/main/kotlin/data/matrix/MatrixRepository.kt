package app.majodesk.data.matrix

/**
 *  (План работы. Рассказывает что объект-репозиторий ДОЛЖЕН уметь делать)
 * */
interface MatrixRepository {


    /**
     * Загружает сохранённую конфигурацию матрицы.
     * @return MatrixConfig или null, если конфигурация отсутствует
     */
    fun loadConfig(): MatrixConfig?


    /**
     * Сохраняет конфигурацию матрицы.
     * @param config объект конфигурации для сохранения
     */
    fun saveConfig(config: MatrixConfig)


    /**
     * Удаляет сохранённую конфигурацию матрицы.
     */
    fun deleteConfig()
}


// А НЕ ДОЛЖНО ЛИ ЭТО БЫТЬ В DOMAIN ???