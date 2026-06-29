// ui/feature/home/components/StatItem.kt
package com.upsjb.movilsantarosa.ui.feature.home.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun StatItem(
    value: String,
    label: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = color
        )
        Text(
            text = label,
            fontSize = 10.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true, name = "Stat Item - Green")
@Composable
fun PreviewStatItemGreen() {
    StatItem(
        value = "20",
        label = "Socios activos",
        color = Color(0xFF4CAF50)
    )
}

@Preview(showBackground = true, name = "Stat Item - Red")
@Composable
fun PreviewStatItemRed() {
    StatItem(
        value = "5",
        label = "Deudores",
        color = Color(0xFFF44336)
    )
}

@Preview(showBackground = true, name = "Stat Item - Blue")
@Composable
fun PreviewStatItemBlue() {
    StatItem(
        value = "15",
        label = "Pagos al día",
        color = Color(0xFF2196F3)
    )
}

@Preview(showBackground = true, name = "Stat Item - Orange")
@Composable
fun PreviewStatItemOrange() {
    StatItem(
        value = "3",
        label = "Multas pendientes",
        color = Color(0xFFFF9800)
    )
}