package app.majodesk.presentation.features.statistics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.majodesk.domain.model.Act
import app.majodesk.domain.repository.ActRecordRepository
import app.majodesk.domain.repository.ActRepository
import app.majodesk.presentation.core.Period
import app.majodesk.presentation.core.localization.stringResource
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

@Composable
fun StatisticsScreen(
    actRepository: ActRepository,
    recordRepository: ActRecordRepository
) {
    var selectedPeriod by remember { mutableStateOf(Period.MONTH) }
    val now = Clock.System.now()
    val allRecords = recordRepository.getAllRecords()
    val records = remember(allRecords, selectedPeriod) {
        val start = selectedPeriod.getStartInstant(now)
        allRecords.filter { start == null || it.startTime >= start }
    }
    val actsMap = remember(actRepository) { actRepository.getAllActs().associateBy { it.id } }

    // Per-activity stats: actId -> (count, sum of value)
    val statsByAct = remember(records) {
        records
            .groupBy { it.actId }
            .mapValues { (_, list) ->
                list.size to list.sumOf { it.value }
            }
            .toList()
            .sortedByDescending { it.second.first } // by count
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        PeriodSelector(
            selectedPeriod = selectedPeriod,
            onPeriodChange = { selectedPeriod = it }
        )
        Spacer(modifier = Modifier.height(16.dp))

        if (records.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource("stats_no_data"),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            return@Column
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource("stats_total_records"),
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = records.size.toString(),
                    style = MaterialTheme.typography.headlineSmall
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource("stats_by_activity"),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(statsByAct) { (actId, pair) ->
                val (count, totalValue) = pair
                val act = actsMap[actId]
                if (act != null) {
                    ActivityStatRow(act = act, recordCount = count, totalValue = totalValue)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PeriodSelector(
    selectedPeriod: Period,
    onPeriodChange: (Period) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = stringResource(selectedPeriod.stringKey()),
            onValueChange = {},
            readOnly = true,
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) }
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            Period.entries.forEach { period ->
                DropdownMenuItem(
                    text = { Text(stringResource(period.stringKey())) },
                    onClick = {
                        onPeriodChange(period)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun ActivityStatRow(
    act: Act,
    recordCount: Int,
    totalValue: Double
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = act.name,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f)
            )
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Text(
                    text = "$recordCount ${stringResource("stats_records_count")}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "${formatValue(totalValue)} ${stringResource("stats_total_value")}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

private fun formatValue(value: Double): String {
    return when {
        value == value.toLong().toDouble() -> value.toLong().toString()
        else -> "%.2f".format(value)
    }
}
