package com.upsjb.movilsantarosa.feature.payments.ui.payment.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.upsjb.movilsantarosa.core.uicomponents.EmptySection
import com.upsjb.movilsantarosa.feature.fine.domain.model.Fine
import com.upsjb.movilsantarosa.feature.fine.ui.fine.components.FineItem
import com.upsjb.movilsantarosa.feature.fine.ui.fine.components.FinesDescription
import com.upsjb.movilsantarosa.feature.payments.domain.model.Payment

@Composable
fun PaymentList(
    payments: List<Payment>,
    onClick: (Payment) -> Unit,
    modifier: Modifier = Modifier
) {
    if (payments.isEmpty()) {

        EmptySection(
            modifier = modifier.fillMaxSize(),
            title = "No hay pagos registrados",
            subtitle = "Aquí aparecerán los pagos cuando estén en el sistema",
        )

    } else {

        LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                bottom = 88.dp
            ),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {

            item {
                PaymentsDescription()
            }

            items(payments) { payment ->
                PaymentItem(
                    payment = payment,
                    onClick = onClick
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FineListEmptyPreview() {
    MaterialTheme {
        PaymentList(
            payments = emptyList(),
            onClick = {}
        )
    }
}