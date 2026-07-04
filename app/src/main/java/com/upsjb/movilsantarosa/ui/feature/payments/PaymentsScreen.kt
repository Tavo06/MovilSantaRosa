package com.upsjb.movilsantarosa.ui.feature.payments

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.unit.dp
import com.upsjb.movilsantarosa.domain.models.PaymentHistory
import com.upsjb.movilsantarosa.domain.models.PaymentMember
import com.upsjb.movilsantarosa.domain.models.PaymentTab
import com.upsjb.movilsantarosa.ui.feature.payments.components.PaymentHistoryList
import com.upsjb.movilsantarosa.ui.feature.payments.components.PaymentMemberCard
import com.upsjb.movilsantarosa.ui.feature.payments.components.PaymentSummary
import com.upsjb.movilsantarosa.ui.feature.payments.components.PaymentsBottomNavigation
import com.upsjb.movilsantarosa.ui.feature.payments.components.PaymentsHeader
import com.upsjb.movilsantarosa.ui.feature.payments.components.RegisterPaymentButton

@Composable
fun PaymentsScreen(
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf(PaymentTab.PAYMENTS) }

    // Datos de ejemplo
    val member = PaymentMember(
        id = "1",
        fullName = "Juan Pérez Gómez",
        dni = "40150411"
    )

    val totalDebt = 50.0

    val paymentHistory = listOf(
        PaymentHistory("Abril", 2025, 50.0, false),
        PaymentHistory("Marzo", 2026, 50.0, true),
        PaymentHistory("Febrero", 2026, 50.0, true),
        PaymentHistory("Enero", 2026, 50.0, true)
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            // Información del socio
            PaymentMemberCard(member = member)

            // Resumen de deuda
            PaymentSummary(totalDebt = totalDebt)

            // Historial de pagos
            PaymentHistoryList(paymentHistory = paymentHistory)

            // Botón registrar pago
            RegisterPaymentButton(
                onRegisterClick = { /* Acción para registrar pago */ }
            )

            // Espacio adicional
            androidx.compose.foundation.layout.Spacer(
                modifier = Modifier.fillMaxWidth().height(16.dp)
            )
        }
    }
}

@Preview(showBackground = true, name = "Payments Screen - Full")
@Composable
fun PreviewPaymentsScreen() {
    PaymentsScreen()
}

@Preview(showBackground = true, name = "Payments Screen - No Debt")
@Composable
fun PreviewPaymentsScreenNoDebt() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        PaymentsHeader()

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            val member = PaymentMember(
                id = "1",
                fullName = "Juan Pérez Gómez",
                dni = "40150411"
            )

            PaymentMemberCard(member = member)
            PaymentSummary(totalDebt = 0.0)

            val paymentHistory = listOf(
                PaymentHistory("Marzo", 2026, 50.0, true),
                PaymentHistory("Febrero", 2026, 50.0, true),
                PaymentHistory("Enero", 2026, 50.0, true)
            )
            PaymentHistoryList(paymentHistory = paymentHistory)

            RegisterPaymentButton(
                onRegisterClick = {}
            )
        }
    }
}