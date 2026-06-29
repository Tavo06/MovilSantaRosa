// ui/feature/register/components/RegisterComponents.kt
package com.upsjb.movilsantarosa.ui.feature.register.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun RegisterProgressIndicator(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.3f)),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(48.dp),
            color = Color(0xFF1A237E)
        )
    }
}

@Composable
fun RegisterErrorDialog(
    message: String,
    onAccept: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = onAccept,
        title = {
            Text("Error de Registro")
        },
        text = {
            Text(message)
        },
        confirmButton = {
            TextButton(onClick = onAccept) {
                Text("Aceptar")
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewRegisterProgressIndicator() {
    RegisterProgressIndicator()
}

@Preview(showBackground = true)
@Composable
fun PreviewRegisterErrorDialog() {
    RegisterErrorDialog(
        message = "Error al registrar el usuario",
        onAccept = {}
    )
}