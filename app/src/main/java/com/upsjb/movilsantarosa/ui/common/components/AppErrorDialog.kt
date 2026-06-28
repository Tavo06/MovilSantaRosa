package com.upsjb.movilsantarosa.ui.common.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun AppErrorDialog(
    message: String,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Error",
                color = MaterialTheme.colorScheme.error
            )
        },
        text = {
            Text(
                text = message,
                color = Color.Black
            )
        },
        confirmButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text("Aceptar")
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun AppErrorDialogPreview() {
    AppErrorDialog(
        message = "Ocurrió un error al realizar la operación.",
        onDismiss = {}
    )
}