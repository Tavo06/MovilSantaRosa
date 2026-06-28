package com.upsjb.movilsantarosa.ui.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

data class StatItem(
    val value: String,
    val label: String,
    val color: Color
)

@Composable
fun AppStatsGrid(
    stats: List<StatItem>,
    columns: Int = 2,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(columns),
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFFE8EAF6))
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(stats) { stat ->
            AppStatItem(
                value = stat.value,
                label = stat.label,
                color = stat.color
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AppStatsGridPreview() {
    MaterialTheme {
        Surface {
            AppStatsGrid(
                stats = listOf(
                    StatItem(
                        value = "120",
                        label = "Socios",
                        color = Color(0xFF1A237E)
                    ),
                    StatItem(
                        value = "15",
                        label = "Pagos",
                        color = Color(0xFF4CAF50)
                    ),
                    StatItem(
                        value = "8",
                        label = "Multas",
                        color = Color(0xFFFF5722)
                    ),
                    StatItem(
                        value = "3",
                        label = "Avisos",
                        color = Color(0xFFFF9800)
                    )
                )
            )
        }
    }
}