package app.majodesk.ui.screens

import RecordsControls
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.majodesk.domain.model.ActRecord
import app.majodesk.domain.repository.ActRecordRepository
import app.majodesk.domain.repository.ActRepository
import app.majodesk.ui.fragments.dialogs.AddRecordDialog
import app.majodesk.ui.fragments.dialogs.EditRecordDialog
import app.majodesk.ui.fragments.lists.RecordsList
import app.majodesk.ui.localization.stringResource
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.time.Duration.Companion.days

@Composable
fun RecordsScreen(
    actRepository: ActRepository,
    recordRepository: ActRecordRepository
) {
    var records by remember { mutableStateOf(recordRepository.getAllRecords()) }
    var showAddDialog by remember { mutableStateOf(false) }
    var recordToEdit by remember { mutableStateOf<ActRecord?>(null) }

    // Состояния для фильтрации и пагинации
    var selectedPeriod by remember { mutableStateOf(Period.ALL) }
    var itemsPerPage by remember { mutableStateOf(50) }
    var currentPage by remember { mutableStateOf(0) }

    // Получаем все акты один раз (можно оптимизировать, но для простоты оставим так)
    val actsMap = remember(actRepository) { actRepository.getAllActs().associateBy { it.id } }

    // Вычисляем отфильтрованные и отсортированные записи
    val now = Clock.System.now()
    val filteredRecords = remember(records, selectedPeriod) {
        val start = selectedPeriod.getStartInstant(now)
        records
            .filter { record ->
                start == null || record.startTime >= start
            }
            .sortedByDescending { it.startTime } // новые сверху
    }

    // Общее количество страниц
    val totalPages = if (filteredRecords.isEmpty()) 1 else (filteredRecords.size + itemsPerPage - 1) / itemsPerPage

    // Корректируем текущую страницу при изменении фильтра
    LaunchedEffect(filteredRecords, itemsPerPage) {
        if (currentPage >= totalPages) {
            currentPage = (totalPages - 1).coerceAtLeast(0)
        }
    }

    // Текущая страница записей
    val pagedRecords = remember(filteredRecords, currentPage, itemsPerPage) {
        val from = currentPage * itemsPerPage
        filteredRecords.drop(from).take(itemsPerPage)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Кнопка Button удалена
            RecordsControls(
                selectedPeriod = selectedPeriod,
                onPeriodChange = { period ->
                    selectedPeriod = period
                    currentPage = 0
                },
                itemsPerPage = itemsPerPage,
                onItemsPerPageChange = { perPage ->
                    itemsPerPage = perPage
                    currentPage = 0
                },
                currentPage = currentPage,
                totalPages = totalPages,
                onPageChange = { newPage -> currentPage = newPage }
            )

            RecordsList(
                records = pagedRecords,
                acts = actsMap,
                onDeleteClick = { record ->
                    recordRepository.deleteRecord(record.id)
                    records = recordRepository.getAllRecords()
                },
                onEditClick = { record ->
                    recordToEdit = record
                },
                modifier = Modifier.weight(1f).fillMaxWidth()
            )
        }

        // FloatingActionButton для добавления записи
        FloatingActionButton(
            onClick = { showAddDialog = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = stringResource("add_record")
            )
        }
    }

    // Диалоги (без изменений)
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

    if (recordToEdit != null) {
        EditRecordDialog(
            record = recordToEdit!!,
            acts = actRepository.getAllActs(),
            onDismiss = { recordToEdit = null },
            onConfirm = { updatedRecord ->
                recordRepository.updateRecord(updatedRecord)
                records = recordRepository.getAllRecords()
                recordToEdit = null
            }
        )
    }
}



enum class Period(val days: Int?) {
    DAY(1),
    THREE_DAYS(3),
    WEEK(7),
    MONTH(30),
    THREE_MONTHS(90),
    HALF_YEAR(180),
    YEAR(365),
    ALL(null);

    fun getStartInstant(now: Instant): Instant? {
        return if (days != null) {
            now - days.days
        } else null
    }
}