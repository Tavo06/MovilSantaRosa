package com.upsjb.movilsantarosa.feature.announcements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.upsjb.movilsantarosa.feature.announcements.components.AnnouncementList
import com.upsjb.movilsantarosa.feature.announcements.components.AnnouncementsDescription

@Composable
fun AnnouncementsScreen(
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf(AnnouncementTab.ANNOUNCEMENTS) }

    // Datos de ejemplo
    val announcements = listOf(
        Announcement(
            "1",
            "Reunión general",
            "Se convoca a todos los socios a la reunión general el día 20 de abril a las 7:00 p.m.",
            "19/04/2026",
            true
        ),
        Announcement(
            "2",
            "Pago obligatorio",
            "Recordamos que el pago mensual debe realizarse antes del 20 de abril.",
            "15/04/2025",
            false
        ),
        Announcement(
            "3",
            "Entrega de chalecos",
            "Se entregarán chalecos nuevos este sábado 12 de abril en la salida de la asociación.",
            "10/04/2025",
            false
        ),
        Announcement(
            "4",
            "Mantenimiento de unidades",
            "Revisar sus unidades para la inspección técnica el 05 de mayo.",
            "06/04/2025",
            false
        )
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        // Descripción
        AnnouncementsDescription()

        // Lista de anuncios
        AnnouncementList(announcements = announcements)

    }
}

@Preview(showBackground = true, name = "Announcements Screen - Full")
@Composable
fun PreviewAnnouncementsScreen() {
    AnnouncementsScreen()
}

@Preview(showBackground = true, name = "Announcements Screen - Empty")
@Composable
fun PreviewAnnouncementsScreenEmpty() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            AnnouncementsDescription()
            AnnouncementList(announcements = emptyList())
        }
    }
}

@Preview(showBackground = true, name = "Announcements Screen - Only Important")
@Composable
fun PreviewAnnouncementsScreenOnlyImportant() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            AnnouncementsDescription()

            val announcements = listOf(
                Announcement(
                    "1",
                    "Reunión general",
                    "Se convoca a todos los socios a la reunión general el día 20 de abril a las 7:00 p.m.",
                    "19/04/2026",
                    true
                ),
                Announcement(
                    "2",
                    "Pago obligatorio",
                    "Recordamos que el pago mensual debe realizarse antes del 20 de abril.",
                    "15/04/2025",
                    true
                )
            )
            AnnouncementList(announcements = announcements)
        }
    }
}