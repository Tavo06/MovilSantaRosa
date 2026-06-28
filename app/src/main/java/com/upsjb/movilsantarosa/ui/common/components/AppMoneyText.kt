package com.upsjb.movilsantarosa.ui.common.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun AppMoneyText(
    amount: Double,
    color: Color = Color(0xFFFF5722),
    modifier: Modifier = Modifier
) {
    Text(
        text = "S/ %.2f".format(amount),
        color = color,
        style = MaterialTheme.typography.titleMedium,
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun AppMoneyTextPreview() {
    AppMoneyText(
        amount = 150.50
    )
}