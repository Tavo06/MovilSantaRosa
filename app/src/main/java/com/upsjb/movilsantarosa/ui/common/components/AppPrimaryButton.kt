package com.upsjb.movilsantarosa.ui.common.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun AppActionButton(
    text: String,
    onClick: () -> Unit,
    backgroundColor: Color = Color(0xFFFF5722),
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = Color.White
        )
    ) {
        Text(text = text)
    }
}

@Preview(showBackground = true)
@Composable
fun AppActionButtonPreview() {
    AppActionButton(
        text = "Registrar Pago",
        onClick = {}
    )
}
@Composable
fun AppPrimaryButton(
    text: String,
    onClick: () -> Unit,
    isLoading: Boolean = false,
    enabled: Boolean = true,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        enabled = enabled && !isLoading,
        modifier = modifier.fillMaxWidth()
    ) {
        if (isLoading) {
            CircularProgressIndicator()
        } else {
            Text(text)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AppPrimaryButtonPreview() {
    AppPrimaryButton(
        text = "Guardar",
        onClick = {}
    )
}

@Composable
fun AppSecondaryButton(
    text: String,
    onClick: () -> Unit,
    enabled: Boolean = true,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.fillMaxWidth()
    ) {
        Text(text)
    }
}

@Preview(showBackground = true)
@Composable
fun AppSecondaryButtonPreview() {
    AppSecondaryButton(
        text = "Cancelar",
        onClick = {}
    )
}