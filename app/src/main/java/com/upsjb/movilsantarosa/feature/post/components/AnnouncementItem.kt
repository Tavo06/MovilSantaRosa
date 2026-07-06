package com.upsjb.movilsantarosa.feature.post.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.upsjb.movilsantarosa.feature.post.Announcement

@Composable
fun AnnouncementItem(
    announcement: Announcement,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Título del anuncio
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween
            ) {
                Text(
                    text = announcement.title,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A237E),
                    fontSize = 16.sp
                )

                if (announcement.isImportant) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFFFF9800)
                    ) {
                        Text(
                            text = "Importante",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                            fontSize = 10.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            // Descripción
            Text(
                text = announcement.description,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                color = Color(0xFF333333),
                fontSize = 14.sp,
                lineHeight = 20.sp
            )

            // Fecha
            Text(
                text = announcement.date,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                color = Color.Gray,
                fontSize = 12.sp,
                textAlign = androidx.compose.ui.text.style.TextAlign.End
            )
        }
    }
}

@Preview(showBackground = true, name = "Announcement Item - Normal")
@Composable
fun PreviewAnnouncementItemNormal() {
    AnnouncementItem(
        announcement = Announcement(
            id = "1",
            title = "Reunión general",
            description = "Se convoca a todos los socios a la reunión general el día 20 de abril a las 7:00 p.m.",
            date = "19/04/2026",
            isImportant = false
        )
    )
}

@Preview(showBackground = true, name = "Announcement Item - Important")
@Composable
fun PreviewAnnouncementItemImportant() {
    AnnouncementItem(
        announcement = Announcement(
            id = "2",
            title = "Pago obligatorio",
            description = "Recordamos que el pago mensual debe realizarse antes del 20 de abril.",
            date = "15/04/2025",
            isImportant = true
        )
    )
}

@Preview(showBackground = true, name = "Announcement Item - Long Description")
@Composable
fun PreviewAnnouncementItemLongDescription() {
    AnnouncementItem(
        announcement = Announcement(
            id = "3",
            title = "Entrega de chalecos",
            description = "Se entregarán chalecos nuevos este sábado 12 de abril en la salida de la asociación. Por favor, traer su DNI para el registro.",
            date = "10/04/2025",
            isImportant = false
        )
    )
}