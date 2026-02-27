package app.majodesk.presentation.fragments.lists

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import app.majodesk.domain.model.Act
import app.majodesk.domain.model.ActType
import app.majodesk.domain.model.Metric
import app.majodesk.presentation.colorFromHex
import app.majodesk.presentation.iconFromName
import app.majodesk.presentation.localization.stringResource
import app.majodesk.domain.model.DistanceUnit
import app.majodesk.domain.model.WeightUnit
import app.majodesk.domain.model.TimeUnit
import app.majodesk.presentation.theme.Dimens
import app.majodesk.presentation.theme.LocalAppSettings


@Composable
fun ActList(
    acts: List<Act>,
    onEditClick: (Act) -> Unit,
    onDeleteClick: (Act) -> Unit,
    modifier: Modifier = Modifier   // добавили параметр modifier
) {
    if (acts.isEmpty()) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource("no_activities"),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    } else {
        val listState = rememberLazyListState()   // состояние для отслеживания прокрутки

        Box(modifier = modifier) {
            LazyColumn(
                state = listState,
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(end = 8.dp), // отступ для скроллбара
                contentPadding = PaddingValues(Dimens.marginElements)
            ) {
                items(acts) { act ->
                    ActCard(
                        act = act,
                        onEditClick = onEditClick,
                        onDeleteClick = onDeleteClick
                    )
                }
            }

            // Вертикальный скроллбар
            VerticalScrollbar(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .fillMaxHeight()
                    .width(8.dp),
                adapter = rememberScrollbarAdapter(listState)
            )
        }
    }
}


@Composable
fun ActCard(act: Act, onEditClick: (Act) -> Unit, onDeleteClick: (Act) -> Unit) {
    val customSettings = LocalAppSettings.current
    val backgroundColor = customSettings.cardBackgroundColor
        ?.let { colorFromHex(it) }
        ?: MaterialTheme.colorScheme.surfaceVariant
    val titleColor = customSettings.cardTitleColor
        ?.let { colorFromHex(it) }
        ?: MaterialTheme.colorScheme.onSurface
    val subtitleColor = customSettings.cardSubtitleColor
        ?.let { colorFromHex(it) }
        ?: MaterialTheme.colorScheme.onSurfaceVariant
    val padding = customSettings.cardPaddingDp?.dp ?: 16.dp

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(padding),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // иконка
            Icon(
                imageVector = iconFromName(act.category.iconName),
                contentDescription = null,
                tint = colorFromHex(act.category.colorHex),
                modifier = Modifier.size(Dimens.iconLarge)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = act.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = titleColor
                )
                Text(
                    text = "${stringResource("category_label")} ${act.category.name}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = subtitleColor
                )


                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = act.type.icon,
                        contentDescription = null,
                        modifier = Modifier.size(Dimens.iconSmall)
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
                            modifier = Modifier.size(Dimens.iconSmall)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = stringResource("regular(short)"),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = formatMetric(act.metric),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                }
            }


            IconButton(onClick = { onEditClick(act) }) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Редактировать",
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.width(4.dp))

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


private val ActType.icon: ImageVector
    get() = when (this) {
        ActType.ACTION -> Icons.Default.CheckCircle
        ActType.HABIT -> Icons.Default.Repeat
        ActType.VICE -> Icons.Default.Warning
    }


// НЕ ЛОКАЛИЗОВАНО!!! И НЕ ЛОКАЛИЗОВАНЫ 3 ВСПОМОГАТЕЛЬНЫЕ ФУНКЦИИ НИЖЕ !!!
/** Вспомогательная ф-я для отображения метрик*/
fun formatMetric(metric: Metric): String {
    return when (metric) {
        is Metric.Count -> "${metric.points} очков за выполнение"
        is Metric.Distance -> "${metric.points} очков за ${unitString(metric.unit)}"
        is Metric.Weight -> "${metric.points} очков за ${unitString(metric.unit)}"
        is Metric.Time -> "${metric.points} очков за ${unitString(metric.unit)}"
    }
}

private fun unitString(unit: DistanceUnit): String = when (unit) {
    DistanceUnit.KILOMETER -> "километр"
    DistanceUnit.METER -> "метр"
}

private fun unitString(unit: WeightUnit): String = when (unit) {
    WeightUnit.KILOGRAM -> "килограмм"
    WeightUnit.TON -> "тонну"
}

private fun unitString(unit: TimeUnit): String = when (unit) {
    TimeUnit.HOUR -> "час"
    TimeUnit.MINUTE -> "мин"
    TimeUnit.SECOND -> "сек"
}
