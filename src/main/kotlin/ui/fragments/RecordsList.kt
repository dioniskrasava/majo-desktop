package app.majodesk.ui.fragments

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.majodesk.domain.model.Act
import app.majodesk.domain.model.ActRecord
import kotlinx.datetime.format.DateTimeComponents
import kotlinx.datetime.format.format

@Composable
fun RecordsList(
    records: List<ActRecord>,
    acts: Map<Long, Act>,
    onDeleteClick: (ActRecord) -> Unit
) {
    if (records.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text("Нет записей")
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
                    onDeleteClick = { onDeleteClick(record) }
                )
            }
        }
    }
}

@Composable
fun RecordCard(
    record: ActRecord,
    act: Act?,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = act?.name ?: "Неизвестная активность",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "Начало: ${record.startTime.toString()}",
                    style = MaterialTheme.typography.bodyMedium
                )
                if (record.durationMinutes != null) {
                    Text(
                        text = "Длительность: ${record.durationMinutes} мин",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                if (record.notes.isNotBlank()) {
                    Text(
                        text = "Заметки: ${record.notes}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
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