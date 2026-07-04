package com.upsjb.movilsantarosa.ui.common.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun MessageDialog(
    title: String,
    message: String,
    onConfirmClick: () -> Unit,
    confirmButtonText: String,
    modifier: Modifier = Modifier,
    cancelButtonText: String? = null,
    onDismiss: () -> Unit = {},
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismiss,
        title = {
            Text(title)
        },
        text = {
            Text(message)
        },
        confirmButton = {
            TextButton(onClick = onConfirmClick) {
                Text(confirmButtonText)
            }
        },
        dismissButton = {
            if (cancelButtonText != null) {
                TextButton(onClick = onDismiss) {
                    Text(cancelButtonText)
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewRegisterErrorDialog() {
    MessageDialog(
        message = "Error al registrar el usuario",
        title = "Aviso",
        confirmButtonText = "Aceptar",
        onConfirmClick = {},
        onDismiss = {}
    )
}