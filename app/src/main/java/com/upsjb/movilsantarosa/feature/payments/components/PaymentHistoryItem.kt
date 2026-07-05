package com.upsjb.movilsantarosa.feature.payments.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.upsjb.movilsantarosa.feature.payments.PaymentHistory

@Composable
fun PaymentHistoryItem(
    payment: PaymentHistory,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Mes y año
        Text(
            text = "${payment.month} ${payment.year}",
            modifier = Modifier.weight(2f),
            fontSize = 14.sp,
            color = Color(0xFF1A237E)
        )

        // Estado
        Surface(
            shape = RoundedCornerShape(8.dp),
            color = if (payment.isPaid)
                Color(0xFF4CAF50).copy(alpha = 0.15f)
            else
                Color(0xFFFF5722).copy(alpha = 0.15f)
        ) {
            Text(
                text = if (payment.isPaid) "Pagado" else "Pendiente",
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                fontSize = 12.sp,
                color = if (payment.isPaid) Color(0xFF4CAF50) else Color(0xFFFF5722),
                fontWeight = FontWeight.Medium
            )
        }

        // Monto
        Text(
            text = "S/ ${String.format("%.2f", payment.amount)}",
            modifier = Modifier.weight(1f),
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF1A237E)
        )
    }
}

@Preview(showBackground = true, name = "Payment History Item - Paid")
@Composable
fun PreviewPaymentHistoryItemPaid() {
    PaymentHistoryItem(
        payment = PaymentHistory(
            month = "Marzo",
            year = 2026,
            amount = 50.0,
            isPaid = true
        )
    )
}

@Preview(showBackground = true, name = "Payment History Item - Pending")
@Composable
fun PreviewPaymentHistoryItemPending() {
    PaymentHistoryItem(
        payment = PaymentHistory(
            month = "Abril",
            year = 2025,
            amount = 50.0,
            isPaid = false
        )
    )
}