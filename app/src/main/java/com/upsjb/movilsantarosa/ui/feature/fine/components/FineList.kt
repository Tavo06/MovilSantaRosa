// ui/feature/fines/components/FineList.kt
package com.upsjb.movilsantarosa.ui.feature.fines.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.upsjb.movilsantarosa.domain.models.Fine

@Composable
fun FineList(
    fines: List<Fine>,
    modifier: Modifier = Modifier
) {
    if (fines.isEmpty()) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center
        ) {
            Text(
                text = "No hay multas registradas",
                color = Color.Gray,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
    } else {
        LazyColumn(
            modifier = modifier,
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(4.dp)
        ) {
            items(fines) { fine ->
                FineItem(fine = fine)
            }
        }
    }
}

@Preview(showBackground = true, name = "Fine List - With Data")
@Composable
fun PreviewFineListWithData() {
    val fines = listOf(
        Fine("1", "Juan Pérez Gómez", "Falla de turno", "25/04/2025", 20.0),
        Fine("2", "Luis Alberto Ramos", "Mal comportamiento", "15/04/2024", 30.0),
        Fine("3", "Maria Torres Silva", "No asistió a reunión", "16/04/2025", 30.0),
        Fine("4", "Carlos Huamán Roca", "Falla de documentos", "05/04/2025", 15.6)
    )
    FineList(fines = fines)
}

@Preview(showBackground = true, name = "Fine List - Empty")
@Composable
fun PreviewFineListEmpty() {
    FineList(fines = emptyList())
}