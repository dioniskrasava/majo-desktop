package app.majodesk.ui.fragments

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.majodesk.domain.model.Act
import app.majodesk.domain.model.ActType
import app.majodesk.ui.colorFromHex
import app.majodesk.ui.iconFromName
import app.majodesk.ui.localization.stringResource

@Composable
fun ActList(
    acts: List<Act>,
    onEditClick: (Act) -> Unit,
    onDeleteClick: (Act) -> Unit,
) {
    if (acts.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource("no_activities"),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    } else {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            items(acts) { act ->
                ActCard(
                    act,
                    onEditClick,
                    onDeleteClick
                )
            }
        }
    }
}


@Composable
fun ActCard(
    act: Act,
    onEditClick: (Act) -> Unit,
    onDeleteClick: (Act) -> Unit

) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = iconFromName(act.category.iconName),
                contentDescription = null,
                tint = colorFromHex(act.category.colorHex),
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = act.name,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "${stringResource("category_label")} ${act.category.name}",
                    style = MaterialTheme.typography.bodyMedium
                )
                // Объединённая строка для типа и регулярности
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = act.type.icon,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = stringResource(
                            when (act.type) {
                                ActType.ACTION -> "type_action"
                                ActType.HABIT -> "type_habit"
                                ActType.VICE -> "type_vice"
                            }
                        ),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    if (act.regularity) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.Default.Repeat,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(16.dp) // согласованный размер
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = stringResource("regular(short)"), // добавьте ключ в локализацию
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }


            // Кнопка редактирования
            IconButton(onClick = { onEditClick(act) }) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Редактировать",
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.width(4.dp))

            // Кнопка удаления
            IconButton(onClick = { onDeleteClick(act) }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Удалить",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

// Удаляем старый extension ActType.displayName, оставляем только icon
private val ActType.icon: androidx.compose.ui.graphics.vector.ImageVector
    get() = when (this) {
        ActType.ACTION -> Icons.Default.CheckCircle
        ActType.HABIT -> Icons.Default.Repeat
        ActType.VICE -> Icons.Default.Warning
    }