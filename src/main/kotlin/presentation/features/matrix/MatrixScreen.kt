package app.majodesk.presentation.features.matrix

import androidx.compose.runtime.*
import app.majodesk.data.matrix.FileMatrixRepository
import app.majodesk.domain.repository.ActRepository
import app.majodesk.domain.repository.ActRecordRepository

/**
 * Точка входа в экран матрицы.
 *
 * @param actRepository репозиторий активностей (для получения списка активностей)
 * @param recordRepository репозиторий записей (для отображения выполненных дней)
 */
@Composable
fun MatrixScreen(
    actRepository: ActRepository,
    recordRepository: ActRecordRepository
) {
    // Создаём репозиторий для работы с конфигурацией матрицы
    val matrixRepository = remember { FileMatrixRepository() }

    // Загружаем сохранённую конфигурацию
    //              ---? Почему именно через by remember ?---
    var config by remember { mutableStateOf(matrixRepository.loadConfig()) }


    // Действие при нажатии "Настроить матрицу" – удаляем текущую конфигурацию и переходим к настройке
    val onReconfigure = {
        matrixRepository.deleteConfig()
        config = null
    }

    // Если конфигурации нет или она пуста, показываем экран настройки порядка,
    // иначе – саму матрицу.
    if (config == null || config!!.orderedActivityIds.isEmpty()) {
        MatrixSetupScreen(
            actRepository = actRepository,
            onConfigSaved = { newConfig ->
                matrixRepository.saveConfig(newConfig)
                config = newConfig
            }
        )
    } else {
        MatrixViewScreen(
            config = config!!,
            actRepository = actRepository,
            recordRepository = recordRepository,
            onReconfigure = onReconfigure   // передаём
        )
    }
}