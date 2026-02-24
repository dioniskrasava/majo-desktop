package app.majodesk.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import app.majodesk.data.matrix.MatrixConfig
import app.majodesk.domain.model.Act
import app.majodesk.domain.model.ActRecord
import app.majodesk.domain.repository.ActRepository
import app.majodesk.domain.repository.ActRecordRepository
import app.majodesk.ui.fragments.dialogs.AddRecordDialog
import kotlinx.datetime.*
import kotlinx.datetime.TimeZone

@Composable
fun MatrixViewScreen(
    config: MatrixConfig,
    actRepository: ActRepository,
    recordRepository: ActRecordRepository
) {
    val actsMap = remember { actRepository.getAllActs().associateBy { it.id } }
    val orderedActs = config.orderedActivityIds.mapNotNull { actsMap[it] }
    var records by remember { mutableStateOf(recordRepository.getAllRecords()) }

    // Период: последние 100 дней (можно сделать настраиваемым)
    val daysCount = 100
    val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    val dates = (0 until daysCount).map { offset ->
        today.minus(offset, DateTimeUnit.DAY)
    }.reversed()

    // Группируем записи по activityId и дате
    val recordsByActAndDate = records
        .groupBy { it.actId }
        .mapValues { entry ->
            entry.value.associateBy { it.startTime.toLocalDateTime(TimeZone.currentSystemDefault()).date }
        }

    // Состояние для диалога добавления
    var showAddDialog by remember { mutableStateOf(false) }
    var selectedActForDialog by remember { mutableStateOf<Act?>(null) }
    var selectedDateForDialog by remember { mutableStateOf<LocalDate?>(null) }

    Column(
        modifier = Modifier.fillMaxSize().padding(8.dp)
    ) {
        // Заголовок с месяцами
        MonthHeader(dates)

        // Строки активностей
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(orderedActs) { act ->
                ActivityRow(
                    act = act,
                    dates = dates,
                    recordsForAct = recordsByActAndDate[act.id] ?: emptyMap(),
                    onCellClick = { date ->
                        selectedActForDialog = act
                        selectedDateForDialog = date
                        showAddDialog = true
                    }
                )
            }
        }
    }

    if (showAddDialog && selectedActForDialog != null && selectedDateForDialog != null) {
        val instant = selectedDateForDialog!!.atStartOfDayIn(TimeZone.currentSystemDefault())
        AddRecordDialog(
            acts = listOf(selectedActForDialog!!), // передаём только выбранную активность
            onDismiss = { showAddDialog = false },
            onConfirm = { newRecord ->
                recordRepository.createRecord(newRecord)
                records = recordRepository.getAllRecords() // обновляем список
                showAddDialog = false
            },
            initialAct = selectedActForDialog,
            initialDateTime = instant
        )
    }
}

@Composable
fun MonthHeader(dates: List<LocalDate>) {
    val months = dates.groupBy { it.month }
    LazyRow(
        modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        items(months.toList()) { (month, monthDates) ->
            Text(
                text = month.name.substring(0, 3), // сокращение месяца
                modifier = Modifier.width((monthDates.size * 20).dp), // ширина под количество дней
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
fun ActivityRow(
    act: Act,
    dates: List<LocalDate>,
    recordsForAct: Map<LocalDate, ActRecord>,
    onCellClick: (LocalDate) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Название активности (фиксированная ширина)
        Text(
            text = act.name,
            modifier = Modifier.width(120.dp),
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 1
        )

        // Квадратики
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            itemsIndexed(dates) { _, date ->
                val record = recordsForAct[date]
                val isGreen = record != null && record.value > 0
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(if (isGreen) Color.Green else Color.LightGray)
                        .clickable { onCellClick(date) }
                )
            }
        }
    }
}