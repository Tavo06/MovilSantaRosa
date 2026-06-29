// ui/feature/payments/components/PaymentMemberCard.kt
package com.upsjb.movilsantarosa.ui.feature.payments.components

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
import androidx.compose.material3.Surface
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
import com.upsjb.movilsantarosa.domain.models.PaymentMember

@Composable
fun PaymentMemberCard(
    member: PaymentMember,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF1A237E).copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = member.fullName.take(2).uppercase(),
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A237E),
                    fontSize = 16.sp
                )
            }

            // Información del socio
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 12.dp)
            ) {
                Text(
                    text = member.fullName,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF1A237E),
                    fontSize = 16.sp
                )
                Text(
                    text = "DNI: ${member.dni}",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            }

            // Estado
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFFFF5722).copy(alpha = 0.15f)
            ) {
                Text(
                    text = "DEUDA",
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                    fontSize = 12.sp,
                    color = Color(0xFFFF5722),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "Payment Member Card")
@Composable
fun PreviewPaymentMemberCard() {
    PaymentMemberCard(
        member = PaymentMember(
            id = "1",
            fullName = "Juan Pérez Gómez",
            dni = "40150411"
        )
    )
}