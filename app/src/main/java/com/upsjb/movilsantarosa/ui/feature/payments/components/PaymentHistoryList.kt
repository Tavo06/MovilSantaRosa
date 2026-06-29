// ui/feature/payments/components/PaymentHistoryList.kt
package com.upsjb.movilsantarosa.ui.feature.payments.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.upsjb.movilsantarosa.domain.models.PaymentHistory

@Composable
fun PaymentHistoryList(
    paymentHistory: List<PaymentHistory>,
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
            Text(
                text = "Historial de pagos",
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A237E),
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            paymentHistory.forEachIndexed { index, payment ->
                PaymentHistoryItem(payment = payment)

                if (index < paymentHistory.size - 1) {
                    HorizontalDivider(
                        color = Color.Gray.copy(alpha = 0.2f),
                        thickness = 1.dp
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "Payment History List")
@Composable
fun PreviewPaymentHistoryList() {
    val payments = listOf(
        PaymentHistory("Abril", 2025, 50.0, false),
        PaymentHistory("Marzo", 2026, 50.0, true),
        PaymentHistory("Febrero", 2026, 50.0, true),
        PaymentHistory("Enero", 2026, 50.0, true)
    )
    PaymentHistoryList(paymentHistory = payments)
}

@Preview(showBackground = true, name = "Payment History List - All Paid")
@Composable
fun PreviewPaymentHistoryListAllPaid() {
    val payments = listOf(
        PaymentHistory("Marzo", 2026, 50.0, true),
        PaymentHistory("Febrero", 2026, 50.0, true),
        PaymentHistory("Enero", 2026, 50.0, true)
    )
    PaymentHistoryList(paymentHistory = payments)
}