package com.upsjb.movilsantarosa.feature.payments.ui.payment_form

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.upsjb.movilsantarosa.core.uicomponents.MessageDialog
import com.upsjb.movilsantarosa.core.uicomponents.ProgressIndicatorOverlay
import com.upsjb.movilsantarosa.core.utils.currentDateString
import com.upsjb.movilsantarosa.feature.fine.data.model.FineReason
import com.upsjb.movilsantarosa.feature.payments.data.model.PaymentMethod
import com.upsjb.movilsantarosa.feature.payments.ui.payment_form.component.PaymentFormActionHandler
import com.upsjb.movilsantarosa.feature.payments.ui.payment_form.component.PaymentFormContent

@Composable
fun PaymentFormScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    onSuccess: () -> Unit,
    openMemberPicker: () -> Unit,
    openFinePicker: (String) -> Unit,
    viewModel: PaymentFormViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var showRegisterDialog by remember { mutableStateOf(false) }

    PaymentFormActionHandler(
        action = state.actionState,
        onReset = viewModel::resetAction,
        onSuccess = onSuccess
    )

    PaymentFormContent(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .fillMaxSize(),
        state = state,
        updateForm = viewModel::updateForm,
        onSaveClick = {
            showRegisterDialog = true
        },
        onEditClick = {
            viewModel.setMode(PaymentFormMode.EDIT)
        },
        onBackClick = onBackClick,
        openMemberPicker = openMemberPicker,
        onMemberChange = {
            if (it == null) {
                viewModel.clearMember()
            } else {
                viewModel.selectMember(it)
            }
        },
        onFineChange = {
            if (it == null) {
                viewModel.clearFine()
            } else {
                viewModel.selectFine(it)
            }
        },
        openFinePicker = openFinePicker
    )

    if (state.actionState is PaymentFormActionState.Loading) {
        ProgressIndicatorOverlay()
    }

    if (showRegisterDialog) {
        MessageDialog(
            title = "Confirmar",
            message = "¿Desea ${state.mode.displayName.lowercase()}?",
            confirmButtonText = "Sí",
            cancelButtonText = "No, cancelar",
            onConfirmClick = {
                showRegisterDialog = false
                viewModel.savePayment()
            },
            onDismiss = {
                showRegisterDialog = false
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PaymentFormScreenPreview() {

    val state = PaymentFormUiState(
        form = PaymentFormState(
            memberName = "Juan Pérez",
            memberEmail = "juan@mail.com",
            memberDniNumber = "72751565",
            fineAmount = 50.0,
            fineReason = FineReason.OTHER,
            paymentMethod = PaymentMethod.YAPE,
            observation = "Pago realizado en oficina"
        ),
        mode = PaymentFormMode.CREATE,
        paymentId = ""
    )

    MaterialTheme {
        PaymentFormContent(
            state = state,
            updateForm = {},
            onSaveClick = {},
            onEditClick = {},
            onBackClick = {},
            openMemberPicker = {},
            onMemberChange = {},
            onFineChange = {},
            openFinePicker = {}
        )
    }
}