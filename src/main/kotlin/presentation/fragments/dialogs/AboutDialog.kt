package app.majodesk.presentation.fragments.dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import app.majodesk.presentation.localization.stringResource

@Composable
fun AboutDialog(
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource("about_app")) },
        text = {
            Column {



                Text(
                    text = "Majo Desktop",
                    style = MaterialTheme.typography.headlineSmall // или headlineMedium
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row {
                    Text(
                        text = buildString {
                            append("${stringResource("version")}: ")
                        },
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)

                    )
                    // Жирная версия отдельно
                    Text(
                        text = "1.0.0",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row {
                    Text(
                        text = buildString {
                            append("${stringResource("author")}: ")
                        },
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = "Belov Ivan",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }


                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = stringResource("description"),
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = stringResource("description_application"),
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(30.dp))
                Text(
                    text = "© 2025",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource("close"))
            }
        }
    )
}