// file: ui/fragments/RecordsList.kt
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import app.majodesk.domain.model.Act
import app.majodesk.domain.model.ActRecord
import app.majodesk.ui.colorFromHex
import app.majodesk.ui.iconFromName
import app.majodesk.ui.theme.Dimens
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.byUnicodePattern
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun RecordsList(
    records: List<ActRecord>,
    acts: Map<Long, Act>,
    onDeleteClick: (ActRecord) -> Unit,
    onEditClick: (ActRecord) -> Unit   // новая функция
) {
    if (records.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Нет записей", // лучше использовать stringResource
                style = MaterialTheme.typography.bodyLarge
            )
        }
    } else {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(records) { record ->
                RecordCard(
                    record = record,
                    act = acts[record.actId],
                    onDeleteClick = { onDeleteClick(record) },
                    onEditClick = { onEditClick(record) }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordCard(
    record: ActRecord,
    act: Act?,
    onDeleteClick: () -> Unit,
    onEditClick: () -> Unit
) {
    val category = act?.category
    val cardColor = if (category != null) {
        colorFromHex(category.colorHex).copy(alpha = 0.15f) // полупрозрачный фон
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

            // Основная информация
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = act?.name ?: "Неизвестная активность",
                    style = MaterialTheme.typography.titleLarge, // увеличенный шрифт
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = formatDateTime(record.startTime),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                if (record.durationMinutes != null) {
                    Text(
                        text = "Длительность: ${record.durationMinutes} мин",
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
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f) // другой цвет
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

// Форматирование даты/времени для отображения
@OptIn(FormatStringsInDatetimeFormats::class)
private fun formatDateTime(instant: kotlinx.datetime.Instant): String {
    val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
    val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
    return localDateTime.toJavaLocalDateTime().format(formatter)
}