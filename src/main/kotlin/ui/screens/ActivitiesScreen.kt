package app.majodesk.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import app.majodesk.domain.model.Act
import app.majodesk.domain.repository.ActRepository
import app.majodesk.domain.repository.CategoryRepository
import app.majodesk.ui.fragments.ActList
import app.majodesk.ui.fragments.AddActCard
import app.majodesk.ui.fragments.AddCategoryDialog
import app.majodesk.ui.fragments.EditActDialog

@Composable
fun <T> ActivitiesScreen(
    repository: T
) where T : ActRepository, T : CategoryRepository {

    var acts by remember { mutableStateOf(repository.getAllActs()) }
    var categories by remember { mutableStateOf(repository.getAllCategories()) }
    var showAddCategoryDialog by remember { mutableStateOf(false) }
    var actToEdit by remember { mutableStateOf<Act?>(null) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AddActCard(
            categories = categories,
            onAddCategoryClick = { showAddCategoryDialog = true },
            onAddClick = { name, category, type, regularity ->
                val act = Act(
                    id = 0,
                    name = name,
                    category = category,
                    type = type,
                    regularity = regularity
                )
                repository.createAct(act)
                acts = repository.getAllActs()
            }
        )
        ActList(
            acts = acts,
            onEditClick = { act -> actToEdit = act },          // открыть диалог редактирования
            onDeleteClick = { act ->
                repository.deleteAct(act.id)
                acts = repository.getAllActs()
            }
        )
    }

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
}