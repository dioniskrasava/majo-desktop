package app.majodesk.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import app.majodesk.domain.model.Act
import app.majodesk.domain.repository.ActRepository
import app.majodesk.ui.fragments.AddActCard


@Composable
fun MainScreen(repository: ActRepository) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AddActCard { name, category, type, regularity ->
                val act = Act(
                    id = 0,
                    name = name,
                    category = category,
                    type = type,
                    regularity = regularity
                )
                repository.createAct(act)

            }
        }
    }
}