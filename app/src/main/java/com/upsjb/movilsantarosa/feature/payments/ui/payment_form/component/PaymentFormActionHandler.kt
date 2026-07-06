package com.upsjb.movilsantarosa.feature.payments.ui.payment_form.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.upsjb.movilsantarosa.core.uicomponents.MessageDialog
import com.upsjb.movilsantarosa.feature.payments.ui.payment_form.PaymentFormActionState

@Composable
fun PaymentFormActionHandler(
    action: PaymentFormActionState,
    onSuccess: () -> Unit,
    onReset: () -> Unit
) {

    LaunchedEffect(action) {
        if (action is PaymentFormActionState.Success) {
            onSuccess()
        }
    }

    if (action is PaymentFormActionState.Error) {
        MessageDialog(
            title = "Aviso",
            message = action.message,
            confirmButtonText = "Aceptar",
            onConfirmClick = onReset,
            onDismiss = onReset
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PaymentFormActionHandlerPreview() {

    var state by remember {
        mutableStateOf<PaymentFormActionState>(
            PaymentFormActionState.Error("No se pudo guardar el pago")
        )
    }

    MaterialTheme {
        Box(modifier = Modifier.fillMaxSize()) {

            PaymentFormActionHandler(
                action = state,
                onReset = {
                    state = PaymentFormActionState.Idle
                },
                onSuccess = {}
            )
        }
    }
}