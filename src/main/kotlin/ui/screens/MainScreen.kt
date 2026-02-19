package app.majodesk.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import app.majodesk.domain.model.Act
import app.majodesk.domain.repository.ActRepository
import app.majodesk.ui.fragments.ActList
import app.majodesk.ui.fragments.AddActCard


@Composable
fun MainScreen(repository: ActRepository) {

    var acts by remember { mutableStateOf(repository.getAllActs()) }

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
                acts = repository.getAllActs()
            }


            ActList(acts = acts)


        }
    }
}