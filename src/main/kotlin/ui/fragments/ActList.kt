package app.majodesk.ui.fragments

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import app.majodesk.domain.model.Act
import app.majodesk.domain.model.ActCategory
import app.majodesk.domain.model.ActType

@Composable
fun ActList(acts: List<Act>) {
    if (acts.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Пока нет активностей.\nДобавьте первую!",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    } else {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(acts) { act ->
                ActCard(act)
            }
        }
    }
}

@Composable
fun ActCard(act: Act) {
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
            // Иконка в зависимости от категории
            Icon(
                imageVector = act.category.icon,
                contentDescription = null,
                tint = act.category.color,
                modifier = Modifier.size(40.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Информация об активности
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = act.name,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "Категория: ${act.category.displayName}",
                    style = MaterialTheme.typography.bodyMedium
                )
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
                        text = act.type.displayName,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            // Индикатор регулярности
            if (act.regularity) {
                Icon(
                    imageVector = Icons.Default.Repeat,
                    contentDescription = "Регулярная",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

// Расширения для отображения и иконок
private val ActCategory.displayName: String
    get() = when (this) {
        ActCategory.SPORT -> "Спорт"
        ActCategory.EDUCATION -> "Образование"
        ActCategory.ANOTHER -> "Другое"
    }

private val ActCategory.icon: ImageVector
    get() = when (this) {
        ActCategory.SPORT -> Icons.Default.FitnessCenter
        ActCategory.EDUCATION -> Icons.Default.MenuBook
        ActCategory.ANOTHER -> Icons.Default.Category
    }

private val ActCategory.color: Color
    get() = when (this) {
        ActCategory.SPORT -> Color(0xFF4CAF50)   // зелёный
        ActCategory.EDUCATION -> Color(0xFF2196F3) // синий
        ActCategory.ANOTHER -> Color(0xFF9E9E9E)   // серый
    }

private val ActType.displayName: String
    get() = when (this) {
        ActType.ACTION -> "Действие"
        ActType.HABIT -> "Привычка"
        ActType.VICE -> "Порок"
    }

private val ActType.icon: ImageVector
    get() = when (this) {
        ActType.ACTION -> Icons.Default.CheckCircle
        ActType.HABIT -> Icons.Default.Repeat
        ActType.VICE -> Icons.Default.Warning
    }