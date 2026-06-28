package com.upsjb.movilsantarosa.ui.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun AppStatItem(
    value: String,
    label: String,
    color: Color = Color(0xFF1A237E),
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier
            .background(Color(0xFFE8EAF6))
            .padding(12.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = value,
            color = color,
            style = MaterialTheme.typography.headlineSmall
        )

        Text(
            text = label,
            color = Color.Gray,
            style = MaterialTheme.typography.bodyMedium
        )

    }

}

@Preview(showBackground = true)
@Composable
fun AppStatItemPreview() {

    Surface {

        AppStatItem(
            value = "120",
            label = "Socios",
            color = Color(0xFF1A237E)
        )

    }

}