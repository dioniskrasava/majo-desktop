package app.majodesk.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.majodesk.domain.model.Act
import app.majodesk.domain.repository.ActRepository
import app.majodesk.domain.repository.CategoryRepository
import app.majodesk.ui.fragments.dialogs.AddActDialog
import app.majodesk.ui.fragments.lists.ActList
import app.majodesk.ui.fragments.forms.AddActCard
import app.majodesk.ui.fragments.dialogs.AddCategoryDialog
import app.majodesk.ui.fragments.dialogs.EditActDialog
import app.majodesk.ui.localization.stringResource

@Composable
fun <T> ActsScreen(
    repository: T
) where T : ActRepository, T : CategoryRepository {
    var acts by remember { mutableStateOf(repository.getAllActs()) }
    var categories by remember { mutableStateOf(repository.getAllCategories()) }
    var showAddCategoryDialog by remember { mutableStateOf(false) }
    var actToEdit by remember { mutableStateOf<Act?>(null) }
    var showAddDialog by remember { mutableStateOf(false) } // состояние для диалога добавления

    Box(modifier = Modifier.fillMaxSize()) {
        ActList(
            acts = acts,
            onEditClick = { act -> actToEdit = act },
            onDeleteClick = { act ->
                repository.deleteAct(act.id)
                acts = repository.getAllActs()
            },
            modifier = Modifier.fillMaxSize()
        )

        // Плавающая кнопка добавления
        FloatingActionButton(
            onClick = { showAddDialog = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = stringResource("add_activity"))
        }
    }

    // Диалог добавления категории
    if (showAddCategoryDialog) {
        AddCategoryDialog(
            onDismiss = { showAddCategoryDialog = false },
            onConfirm = { newCategory ->
                repository.addCategory(newCategory)
                categories = repository.getAllCategories()
                showAddCategoryDialog = false
            }
        )
    }

    // Диалог редактирования активности
    if (actToEdit != null) {
        EditActDialog(
            act = actToEdit!!,
            categories = categories,
            onDismiss = { actToEdit = null },
            onConfirm = { updatedAct ->
                repository.updateAct(updatedAct)
                acts = repository.getAllActs()
                actToEdit = null
            }
        )
    }

    // Диалог добавления активности
    if (showAddDialog) {
        AddActDialog(
            categories = categories,
            onDismiss = { showAddDialog = false },
            onConfirm = { newAct ->
                repository.createAct(newAct)
                acts = repository.getAllActs()
                showAddDialog = false
            }
        )
    }
}