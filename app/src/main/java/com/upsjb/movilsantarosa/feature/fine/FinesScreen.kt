package com.upsjb.movilsantarosa.feature.fine

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.upsjb.movilsantarosa.feature.fine.components.FineList
import com.upsjb.movilsantarosa.feature.fine.components.FinesDescription
import com.upsjb.movilsantarosa.feature.fine.components.FinesHeader
import kotlin.collections.emptyList

@Composable
fun FinesScreen(
    modifier: Modifier = Modifier
) {

    // Datos de ejemplo
    val fines = listOf(
        Fine("1", "Juan Pérez Gómez", "Falla de turno", "25/04/2025", 20.0),
        Fine("2", "Luis Alberto Ramos", "Mal comportamiento", "15/04/2024", 30.0),
        Fine("3", "Maria Torres Silva", "No asistió a reunión", "16/04/2025", 30.0),
        Fine("4", "Carlos Huamán Roca", "Falla de documentos", "05/04/2025", 15.6)
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        // Descripción
        FinesDescription()

        // Lista de multas
        FineList(fines = fines)

    }
}

@Preview(showBackground = true, name = "Fines Screen - Full")
@Composable
fun PreviewFinesScreen() {
    FinesScreen()
}

@Preview(showBackground = true, name = "Fines Screen - Empty")
@Composable
fun PreviewFinesScreenEmpty() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        FinesHeader()

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            FinesDescription()
            FineList(fines = emptyList())
        }

    }
}