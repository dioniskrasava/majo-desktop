package app.majodesk.presentation.screens

import androidx.compose.runtime.*
import app.majodesk.data.matrix.FileMatrixRepository
import app.majodesk.domain.repository.ActRepository
import app.majodesk.domain.repository.ActRecordRepository

@Composable
fun MatrixScreen(
    actRepository: ActRepository,
    recordRepository: ActRecordRepository
) {
    val matrixRepository = remember { FileMatrixRepository() }
    var config by remember { mutableStateOf(matrixRepository.loadConfig()) }

    val onReconfigure = {
        matrixRepository.deleteConfig()
        config = null
    }

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