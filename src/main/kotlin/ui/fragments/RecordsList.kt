package app.majodesk.ui.fragments

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import app.majodesk.domain.model.Act
import app.majodesk.domain.model.ActRecord
import app.majodesk.domain.model.Metric
import app.majodesk.domain.model.DistanceUnit
import app.majodesk.domain.model.WeightUnit
import app.majodesk.domain.model.TimeUnit
import app.majodesk.ui.colorFromHex
import app.majodesk.ui.iconFromName
import app.majodesk.ui.localization.stringResource
import kotlinx.datetime.*
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import java.time.format.DateTimeFormatter

@Composable
fun RecordsList(
    records: List<ActRecord>,
    acts: Map<Long, Act>,
    onDeleteClick: (ActRecord) -> Unit,
    onEditClick: (ActRecord) -> Unit
) {
    if (records.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource("no_records"),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    } else {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(records) { record ->
                val act = acts[record.actId]
                RecordCard(
                    record = record,
                    act = act,
                    onDeleteClick = { onDeleteClick(record) },
                    onEditClick = { onEditClick(record) }
                )
            }
        }
    }
}

@Composable
fun RecordCard(
    record: ActRecord,
    act: Act?,
    onDeleteClick: () -> Unit,
    onEditClick: () -> Unit
) {
    val category = act?.category
    val cardColor = if (category != null) {
        colorFromHex(category.colorHex).copy(alpha = 0.15f)
    } else {
        MaterialTheme.colorScheme.surfaceVariant
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Иконка категории
            Icon(
                imageVector = iconFromName(category?.iconName ?: "category"),
                contentDescription = null,
                tint = if (category != null) colorFromHex(category.colorHex) else MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.5f))
                    .padding(8.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Информация о записи
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = act?.name ?: "Неизвестная активность",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = formatDateTime(record.startTime),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                if (act != null) {
                    Text(
                        text = formatValueWithUnit(record.value, act.metric),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                if (record.notes.isNotBlank()) {
                    Text(
                        text = record.notes,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontStyle = FontStyle.Italic
                        ),
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
                    )
                }
            }

            // Кнопки действий
            Row {
                IconButton(onClick = onEditClick) {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = "Редактировать",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                IconButton(onClick = onDeleteClick) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Удалить",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

@OptIn(FormatStringsInDatetimeFormats::class)
private fun formatDateTime(instant: Instant): String {
    val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
    val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
    return localDateTime.toJavaLocalDateTime().format(formatter)
}

@Composable
private fun formatValueWithUnit(value: Double, metric: Metric): String {
    return when (metric) {
        is Metric.Count -> "$value ${stringResource("unit_pcs")}"
        is Metric.Distance -> "$value ${unitString(metric.unit)}"
        is Metric.Weight -> "$value ${unitString(metric.unit)}"
        is Metric.Time -> "$value ${unitString(metric.unit)}"
    }
}

private fun unitString(unit: DistanceUnit): String = when (unit) {
    DistanceUnit.KILOMETER -> "км"
    DistanceUnit.METER -> "м"
}
private fun unitString(unit: WeightUnit): String = when (unit) {
    WeightUnit.KILOGRAM -> "кг"
    WeightUnit.TON -> "т"
}
private fun unitString(unit: TimeUnit): String = when (unit) {
    TimeUnit.HOUR -> "ч"
    TimeUnit.MINUTE -> "мин"
    TimeUnit.SECOND -> "сек"
}