package com.upsjb.movilsantarosa.feature.announcements.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import com.upsjb.movilsantarosa.feature.announcements.Announcement

@Composable
fun AnnouncementList(
    announcements: List<Announcement>,
    modifier: Modifier = Modifier
) {
    if (announcements.isEmpty()) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center
        ) {
            Text(
                text = "No hay anuncios disponibles",
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
            items(announcements) { announcement ->
                AnnouncementItem(announcement = announcement)
            }
        }
    }
}

@Preview(showBackground = true, name = "Announcement List - With Data")
@Composable
fun PreviewAnnouncementListWithData() {
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
    AnnouncementList(announcements = announcements)
}

@Preview(showBackground = true, name = "Announcement List - Empty")
@Composable
fun PreviewAnnouncementListEmpty() {
    AnnouncementList(announcements = emptyList())
}

@Preview(showBackground = true, name = "Announcement List - Only Important")
@Composable
fun PreviewAnnouncementListOnlyImportant() {
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