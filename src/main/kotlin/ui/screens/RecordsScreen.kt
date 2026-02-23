package app.majodesk.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.majodesk.domain.model.ActRecord
import app.majodesk.domain.repository.ActRecordRepository
import app.majodesk.domain.repository.ActRepository
import app.majodesk.ui.fragments.AddRecordDialog
import app.majodesk.ui.fragments.RecordsList
import app.majodesk.ui.localization.stringResource

@Composable
fun RecordsScreen(
    actRepository: ActRepository,
    recordRepository: ActRecordRepository
) {
    var records by remember { mutableStateOf(recordRepository.getAllRecords()) }
    var showAddDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = { showAddDialog = true },
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Text(stringResource("add_record")) // нужно добавить ключ в локализацию
        }

        RecordsList(
            records = records,
            acts = actRepository.getAllActs().associateBy { it.id }, // для быстрого получения Act по id
            onDeleteClick = { record ->
                recordRepository.deleteRecord(record.id)
                records = recordRepository.getAllRecords()
            }
        )
    }

    if (showAddDialog) {
        AddRecordDialog(
            acts = actRepository.getAllActs(),
            onDismiss = { showAddDialog = false },
            onConfirm = { newRecord ->
                recordRepository.createRecord(newRecord)
                records = recordRepository.getAllRecords()
                showAddDialog = false
            }
        )
    }
}