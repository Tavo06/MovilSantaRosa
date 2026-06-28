package com.upsjb.movilsantarosa.ui.common.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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