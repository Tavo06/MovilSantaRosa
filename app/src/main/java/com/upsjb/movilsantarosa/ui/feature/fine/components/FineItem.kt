// ui/feature/fines/components/FineItem.kt
package com.upsjb.movilsantarosa.ui.feature.fines.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.upsjb.movilsantarosa.domain.models.Fine

@Composable
fun FineItem(
    fine: Fine,
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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar con iniciales
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFFF5722).copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = fine.memberName.take(2).uppercase(),
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFF5722),
                    fontSize = 16.sp
                )
            }

            // Información de la multa
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 12.dp)
            ) {
                Text(
                    text = fine.memberName,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A237E),
                    fontSize = 16.sp
                )
                Text(
                    text = "Motivo: ${fine.reason}",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            }

            // Monto y fecha
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "S/ ${String.format("%.2f", fine.amount)}",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFF5722),
                    fontSize = 16.sp
                )
                Text(
                    text = fine.date,
                    color = Color.Gray,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "Fine Item")
@Composable
fun PreviewFineItem() {
    FineItem(
        fine = Fine(
            id = "1",
            memberName = "Juan Pérez Gómez",
            reason = "Falla de turno",
            date = "25/04/2025",
            amount = 20.0
        )
    )
}

@Preview(showBackground = true, name = "Fine Item - Different Amount")
@Composable
fun PreviewFineItemDifferentAmount() {
    FineItem(
        fine = Fine(
            id = "2",
            memberName = "Carlos Huamán Roca",
            reason = "Falla de documentos",
            date = "05/04/2025",
            amount = 15.60
        )
    )
}