package app.majodesk.presentation.features.matrix

import androidx.compose.foundation.HorizontalScrollbar
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import app.majodesk.data.matrix.MatrixConfig
import app.majodesk.domain.model.Act
import app.majodesk.domain.model.ActRecord
import app.majodesk.domain.model.ActType
import app.majodesk.domain.repository.ActRepository
import app.majodesk.domain.repository.ActRecordRepository
import app.majodesk.presentation.features.records.components.AddRecordDialog
import app.majodesk.presentation.core.localization.stringResource
import kotlinx.datetime.*
import kotlinx.datetime.TimeZone

/**
 * Экран отображения матрицы с активностями по дням.
 *
 * @param config конфигурация (порядок активностей)
 * @param actRepository репозиторий для получения данных об активностях
 * @param recordRepository репозиторий для получения записей и добавления новых
 * @param onReconfigure действие при нажатии кнопки "Настроить матрицу"
 */
@Composable
fun MatrixViewScreen(
    config: MatrixConfig,
    actRepository: ActRepository,
    recordRepository: ActRecordRepository,
    onReconfigure: () -> Unit
) {
    // Сопоставляем id активностей с объектами Act для быстрого доступа
    val actsMap = remember { actRepository.getAllActs().associateBy { it.id } }

    // Отфильтрованный список активностей в порядке, заданном конфигурацией
    val orderedActs = config.orderedActivityIds.mapNotNull { actsMap[it] }

    // Состояние для хранения всех записей (обновляется после добавления/удаления)
    var records by remember { mutableStateOf(recordRepository.getAllRecords()) }

    // Количество отображаемых дней (можно менять через селектор внизу)
    var daysCount by remember { mutableStateOf(30) }

    // Сегодняшняя дата
    val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

    // Список дат за последние daysCount дней (от earliest до today)
    val dates = (0 until daysCount).map { offset ->
        today.minus(offset, DateTimeUnit.DAY)
    }.reversed()

    // Группируем записи по id активности и по дате для быстрого доступа
    val recordsByActAndDate = records
        .groupBy { it.actId }
        .mapValues { entry ->
            entry.value.associateBy { it.startTime.toLocalDateTime(TimeZone.currentSystemDefault()).date }
        }

    // Состояния для диалога добавления записи
    var showAddDialog by remember { mutableStateOf(false) }
    var selectedActForDialog by remember { mutableStateOf<Act?>(null) }
    var selectedDateForDialog by remember { mutableStateOf<LocalDate?>(null) }

    // Параметры для расчёта ширины ячеек и горизонтальной прокрутки
    val cellWidth = 20.dp
    val spacing = 2.dp
    val totalWidth = (dates.size * (cellWidth + spacing)) - spacing
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier.fillMaxSize().padding(8.dp)
    ) {
        // Верхняя панель с заголовком и кнопкой настройки
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource("matrix_title"),  // можно добавить новый ключ или оставить "Матрица"
                style = MaterialTheme.typography.headlineSmall
            )
            Button(onClick = onReconfigure) {
                Icon(Icons.Default.Settings, contentDescription = null)
                Spacer(modifier = Modifier.width(4.dp))
                Text(stringResource("reconfigure_matrix"))  // новый ключ
            }
        }


        // Шапка с месяцами и числами (горизонтальный скролл)
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.width(120.dp))
            MonthHeader(
                dates = dates,
                scrollState = scrollState,
                cellWidth = cellWidth,
                spacing = spacing,
                totalWidth = totalWidth
            )
        }

        // Список активностей с ячейками
        LazyColumn(
            modifier = Modifier.weight(1f),
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
                    },
                    scrollState = scrollState,
                    cellWidth = cellWidth,
                    spacing = spacing,
                    totalWidth = totalWidth
                )
            }
        }

        // Горизонтальный скроллбар для прокрутки дат
        HorizontalScrollbar(
            modifier = Modifier.fillMaxWidth(),
            adapter = rememberScrollbarAdapter(scrollState)
        )

        // Селектор количества отображаемых дней
        IntervalSelector(
            daysCount = daysCount,
            onIntervalChange = { daysCount = it }
        )


    }

    // Диалог добавления записи (вызывается при клике на ячейку)
    if (showAddDialog && selectedActForDialog != null && selectedDateForDialog != null) {
        val instant = selectedDateForDialog!!.atStartOfDayIn(TimeZone.currentSystemDefault())
        AddRecordDialog(
            acts = listOf(selectedActForDialog!!),
            onDismiss = { showAddDialog = false },
            onConfirm = { newRecord ->
                recordRepository.createRecord(newRecord)
                records = recordRepository.getAllRecords()
                showAddDialog = false
            },
            initialAct = selectedActForDialog,
            initialDateTime = instant
        )
    }
}

/**
 * Шапка с названиями месяцев и числами.
 *
 * @param dates список дат
 * @param scrollState состояние горизонтальной прокрутки
 * @param cellWidth ширина одной ячейки
 * @param spacing отступ между ячейками
 * @param totalWidth общая ширина всех ячеек с отступами
 */
@Composable
fun MonthHeader(
    dates: List<LocalDate>,
    scrollState: ScrollState,
    cellWidth: Dp,
    spacing: Dp,
    totalWidth: Dp
) {
    // Группируем даты по году и месяцу
    val monthGroups = dates.groupBy { it.year to it.month }
        .toSortedMap(compareBy { it.first * 100 + it.second.ordinal })
    val showYear = dates.map { it.year }.distinct().size > 1

    Row(
        modifier = Modifier
            .horizontalScroll(scrollState)
            .width(totalWidth)
    ) {
        monthGroups.forEach { (yearMonth, monthDates) ->
            val (year, month) = yearMonth
            Column(
                modifier = Modifier.width((monthDates.size * (cellWidth + spacing)) - spacing)
            ) {
                // Название месяца (и год, если есть)
                Text(
                    text = if (showYear) "${month.name.take(3)} ${year % 100}" else month.name.take(3),
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 2.dp)
                )
                // Числа
                Row(
                    horizontalArrangement = Arrangement.spacedBy(spacing)
                ) {
                    monthDates.forEach { date ->
                        Text(
                            text = date.dayOfMonth.toString(),
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.width(cellWidth)
                        )
                    }
                }
            }
        }
    }
}

/**
 * Строка для одной активности с ячейками по дням.
 *
 * @param act активность
 * @param dates список дат
 * @param recordsForAct словарь записей для этой активности по датам
 * @param onCellClick callback при клике на ячейку (передаёт дату)
 * @param scrollState состояние горизонтальной прокрутки
 * @param cellWidth ширина ячейки
 * @param spacing отступ между ячейками
 * @param totalWidth общая ширина
 */
@Composable
fun ActivityRow(
    act: Act,
    dates: List<LocalDate>,
    recordsForAct: Map<LocalDate, ActRecord>,
    onCellClick: (LocalDate) -> Unit,
    scrollState: ScrollState,
    cellWidth: Dp,
    spacing: Dp,
    totalWidth: Dp
) {

    val myGreen = Color(1, 118, 1, 255) // зелёный для выполненных

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Название активности (фиксированная ширина)
        Text(
            text = act.name,
            modifier = Modifier.width(120.dp),
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 1
        )

        // Горизонтальный ряд ячеек с прокруткой
        Row(
            modifier = Modifier
                .horizontalScroll(scrollState)
                .width(totalWidth),
            horizontalArrangement = Arrangement.spacedBy(spacing)
        ) {
            dates.forEach { date ->
                val record = recordsForAct[date]

                val backgroundColor = when {
                    record == null -> Color.LightGray                     // нет записи
                    act.type == ActType.VICE -> {
                        // Для пороков: красный, если значение > 0, иначе зелёный
                        // (если запись есть, но значение 0? Но обычно значение >0 означает, что порок совершён)
                        if (record.value > 0) Color.Red else myGreen       // >0 – красный, иначе (0) – зелёный
                    }
                    else -> {
                        // Для обычных активностей: зелёный, если значение > 0 (выполнено), иначе серый
                        if (record.value > 0) myGreen else Color.LightGray
                    }
                }

                Box(
                    modifier = Modifier
                        .size(cellWidth)
                        .clip(RoundedCornerShape(4.dp))
                        .background(backgroundColor)
                        .clickable { onCellClick(date) }
                )
            }
        }
    }
}

/**
 * Селектор для выбора количества отображаемых дней.
 *
 * @param daysCount текущее значение
 * @param onIntervalChange callback при изменении
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IntervalSelector(daysCount: Int, onIntervalChange: (Int) -> Unit) {
    val options = listOf(7, 30, 60, 90, 180, 365)
    var expanded by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("Показать за:", modifier = Modifier.padding(end = 8.dp))
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = "$daysCount дней",
                onValueChange = {},
                readOnly = true,
                modifier = Modifier.menuAnchor().width(120.dp)
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text("$option дней") },
                        onClick = {
                            onIntervalChange(option)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}