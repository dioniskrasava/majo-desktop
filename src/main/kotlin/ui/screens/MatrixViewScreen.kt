package app.majodesk.ui.screens

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
import app.majodesk.ui.fragments.dialogs.AddRecordDialog
import app.majodesk.ui.localization.stringResource
import kotlinx.datetime.*
import kotlinx.datetime.TimeZone

@Composable
fun MatrixViewScreen(
    config: MatrixConfig,
    actRepository: ActRepository,
    recordRepository: ActRecordRepository,
    onReconfigure: () -> Unit
) {
    val actsMap = remember { actRepository.getAllActs().associateBy { it.id } }
    val orderedActs = config.orderedActivityIds.mapNotNull { actsMap[it] }
    var records by remember { mutableStateOf(recordRepository.getAllRecords()) }
    var daysCount by remember { mutableStateOf(30) }
    val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    val dates = (0 until daysCount).map { offset ->
        today.minus(offset, DateTimeUnit.DAY)
    }.reversed()
    val recordsByActAndDate = records
        .groupBy { it.actId }
        .mapValues { entry ->
            entry.value.associateBy { it.startTime.toLocalDateTime(TimeZone.currentSystemDefault()).date }
        }
    var showAddDialog by remember { mutableStateOf(false) }
    var selectedActForDialog by remember { mutableStateOf<Act?>(null) }
    var selectedDateForDialog by remember { mutableStateOf<LocalDate?>(null) }

    val cellWidth = 20.dp
    val spacing = 2.dp
    val totalWidth = (dates.size * (cellWidth + spacing)) - spacing
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier.fillMaxSize().padding(8.dp)
    ) {

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

        HorizontalScrollbar(
            modifier = Modifier.fillMaxWidth(),
            adapter = rememberScrollbarAdapter(scrollState)
        )

        IntervalSelector(
            daysCount = daysCount,
            onIntervalChange = { daysCount = it }
        )


    }

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

@Composable
fun MonthHeader(
    dates: List<LocalDate>,
    scrollState: ScrollState,
    cellWidth: Dp,
    spacing: Dp,
    totalWidth: Dp
) {
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
                Text(
                    text = if (showYear) "${month.name.take(3)} ${year % 100}" else month.name.take(3),
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 2.dp)
                )
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

    val myGreen = Color(0, 150, 0)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = act.name,
            modifier = Modifier.width(120.dp),
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 1
        )
        Row(
            modifier = Modifier
                .horizontalScroll(scrollState)
                .width(totalWidth),
            horizontalArrangement = Arrangement.spacedBy(spacing)
        ) {
            dates.forEach { date ->
                val record = recordsForAct[date]

                val backgroundColor = when {
                    act.type == ActType.VICE && record != null -> Color.Red
                    record?.value?.let { it > 0 } == true -> myGreen
                    else -> Color.LightGray
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IntervalSelector(daysCount: Int, onIntervalChange: (Int) -> Unit) {
    val options = listOf(30, 60, 90, 180, 365)
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