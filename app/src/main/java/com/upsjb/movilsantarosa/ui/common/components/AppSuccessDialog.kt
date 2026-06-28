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
fun AppSuccessDialog(
    message: String,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Éxito",
                color = Color(0xFF4CAF50)
            )
        },
        text = {
            Text(
                text = message,
                color = MaterialTheme.colorScheme.onSurface
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
fun AppSuccessDialogPreview() {
    AppSuccessDialog(
        message = "La operación se realizó correctamente.",
        onDismiss = {}
    )
}