package app.majodesk.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import app.majodesk.domain.model.Act
import app.majodesk.domain.repository.ActRepository
import app.majodesk.domain.repository.CategoryRepository
import app.majodesk.ui.fragments.lists.ActList
import app.majodesk.ui.fragments.forms.AddActCard
import app.majodesk.ui.fragments.dialogs.AddCategoryDialog
import app.majodesk.ui.fragments.dialogs.EditActDialog

@Composable
fun <T> ActsScreen(
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
            onAddClick = { name, category, type, regularity, metric ->
                val act = Act(
                    id = 0, // id будет присвоен в репозитории
                    name = name,
                    category = category,
                    type = type,
                    regularity = regularity,
                    metric = metric
                )
                repository.createAct(act)
                acts = repository.getAllActs()
            }
        )
        ActList(
            acts = acts,
            onEditClick = { act -> actToEdit = act },
            onDeleteClick = { act ->
                repository.deleteAct(act.id)
                acts = repository.getAllActs()
            },
            modifier = Modifier.weight(1f).fillMaxWidth()
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