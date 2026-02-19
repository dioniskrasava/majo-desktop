package app.majodesk.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import app.majodesk.domain.model.Act
import app.majodesk.domain.model.ActCategory
import app.majodesk.domain.repository.ActRepository
import app.majodesk.domain.repository.CategoryRepository
import app.majodesk.ui.fragments.ActList
import app.majodesk.ui.fragments.AddActCard
import app.majodesk.ui.fragments.AddCategoryDialog // создайте этот компонент

@Composable
fun MainScreen(repository: ActRepository, categoryRepository: CategoryRepository) {
    var acts by remember { mutableStateOf(repository.getAllActs()) }
    var categories by remember { mutableStateOf(categoryRepository.getAllCategories()) }
    var showAddCategoryDialog by remember { mutableStateOf(false) }

    Surface(modifier = Modifier.fillMaxSize()) {
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
                    acts = repository.getAllActs() // обновляем список после добавления
                }
            )

            ActList(acts = acts)
        }
    }

    if (showAddCategoryDialog) {
        AddCategoryDialog(
            onDismiss = { showAddCategoryDialog = false },
            onConfirm = { newCategory ->
                categoryRepository.addCategory(newCategory)
                categories = categoryRepository.getAllCategories() // обновляем список категорий
                showAddCategoryDialog = false
            }
        )
    }
}