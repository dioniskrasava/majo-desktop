package app.majodesk.presentation.features.matrix

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.majodesk.data.matrix.MatrixConfig
import app.majodesk.domain.model.Act
import app.majodesk.domain.repository.ActRepository
import app.majodesk.presentation.colorFromHex
import app.majodesk.presentation.iconFromName
import app.majodesk.presentation.core.theme.Dimens

@Composable
fun MatrixSetupScreen(
    actRepository: ActRepository,
    onConfigSaved: (MatrixConfig) -> Unit
) {
    val allActs = remember { actRepository.getAllActs() }
    var orderedActs by remember { mutableStateOf(allActs) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Настройка порядка активностей",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.weight(1f)
        ) {
            itemsIndexed(orderedActs) { index, act ->
                ActivityOrderItem(
                    act = act,
                    onMoveUp = {
                        if (index > 0) {
                            orderedActs = orderedActs.toMutableList().apply {
                                val temp = this[index]
                                this[index] = this[index - 1]
                                this[index - 1] = temp
                            }
                        }
                    },
                    onMoveDown = {
                        if (index < orderedActs.lastIndex) {
                            orderedActs = orderedActs.toMutableList().apply {
                                val temp = this[index]
                                this[index] = this[index + 1]
                                this[index + 1] = temp
                            }
                        }
                    },
                    isFirst = index == 0,
                    isLast = index == orderedActs.lastIndex
                )
            }
        }

        Button(
            onClick = {
                val config = MatrixConfig(orderedActs.map { it.id })
                onConfigSaved(config)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text("Создать матрицу")
        }
    }
}

@Composable
fun ActivityOrderItem(
    act: Act,
    onMoveUp: () -> Unit,
    onMoveDown: () -> Unit,
    isFirst: Boolean,
    isLast: Boolean
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
            Icon(
                imageVector = iconFromName(act.category.iconName),
                contentDescription = null,
                tint = colorFromHex(act.category.colorHex),
                modifier = Modifier.size(Dimens.iconLarge)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = act.name, style = MaterialTheme.typography.titleMedium)
                Text(
                    text = act.category.name,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Row {
                IconButton(onClick = onMoveUp, enabled = !isFirst) {
                    Icon(Icons.Default.ArrowUpward, contentDescription = "Вверх")
                }
                IconButton(onClick = onMoveDown, enabled = !isLast) {
                    Icon(Icons.Default.ArrowDownward, contentDescription = "Вниз")
                }
            }
        }
    }
}