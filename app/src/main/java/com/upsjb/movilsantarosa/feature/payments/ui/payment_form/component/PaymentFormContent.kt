package com.upsjb.movilsantarosa.feature.payments.ui.payment_form.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.PersonSearch
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.upsjb.movilsantarosa.core.uicomponents.AppHeader
import com.upsjb.movilsantarosa.core.uicomponents.AppPrimaryButton
import com.upsjb.movilsantarosa.core.uicomponents.CardContent
import com.upsjb.movilsantarosa.core.uicomponents.FormDatePicker
import com.upsjb.movilsantarosa.core.uicomponents.FormDropdown
import com.upsjb.movilsantarosa.core.uicomponents.FormTextField
import com.upsjb.movilsantarosa.core.utils.toCurrencyString
import com.upsjb.movilsantarosa.core.utils.toDateString
import com.upsjb.movilsantarosa.feature.auth.domain.model.UserRole
import com.upsjb.movilsantarosa.feature.fine.data.model.FineReason
import com.upsjb.movilsantarosa.feature.fine.domain.model.Fine
import com.upsjb.movilsantarosa.feature.members.domain.model.Member
import com.upsjb.movilsantarosa.feature.payments.data.model.PaymentMethod
import com.upsjb.movilsantarosa.feature.payments.ui.payment_form.PaymentFormMode
import com.upsjb.movilsantarosa.feature.payments.ui.payment_form.PaymentFormState
import com.upsjb.movilsantarosa.feature.payments.ui.payment_form.PaymentFormUiState

@Composable
fun PaymentFormContent(
    modifier: Modifier = Modifier,
    state: PaymentFormUiState,
    onMemberChange: (Member?) -> Unit,
    onFineChange: (Fine?) -> Unit,
    updateForm: (PaymentFormState.() -> PaymentFormState) -> Unit,
    onSaveClick: () -> Unit,
    onEditClick: () -> Unit,
    onBackClick: () -> Unit,
    openMemberPicker: () -> Unit,
    openFinePicker: (String) -> Unit,
) {

    val form = state.form
    val isReadOnly = state.mode == PaymentFormMode.READ_ONLY
    val isCreateMode = state.mode == PaymentFormMode.CREATE

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {

        AppHeader(
            modifier = Modifier.padding(top = 16.dp),
            title = state.mode.displayName,
            onBackClick = onBackClick,
            actions = {
                if (!isCreateMode && state.role == UserRole.ADMIN) {
                    IconButton(
                        onClick = onEditClick,
                        modifier = Modifier.background(
                            MaterialTheme.colorScheme.primary,
                            CircleShape
                        )
                    ) {
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }
        )

        FormTextField(
            value = form.memberName,
            onValueChange = { updateForm { copy(memberName = it) } },
            label = "Nombre del socio",
            enabled = !isReadOnly && isCreateMode,
            readOnly = true,
            leadingIcon = {
                IconButton(onClick = openMemberPicker) {
                    Icon(
                        imageVector = Icons.Default.PersonSearch,
                        contentDescription = null
                    )
                }
            },
            trailingIcon = {
                if (form.isMemberFilled) {
                    IconButton(
                        onClick = { onMemberChange(null) }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Quitar socio"
                        )
                    }
                }
            }
        )

        if (form.isMemberFilled) {
            CardContent(containerColor = MaterialTheme.colorScheme.secondaryContainer) {
                Text("DNI: ${form.memberDniNumber}", fontWeight = FontWeight.Medium)
                Text("Correo: ${form.memberEmail}", fontWeight = FontWeight.Medium)
            }
            FormTextField(
                value = form.fineId,
                onValueChange = {},
                label = "Multa",
                enabled = !isReadOnly && isCreateMode,
                readOnly = true,
                leadingIcon = {
                    IconButton(onClick = { openFinePicker(form.memberEmail) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ReceiptLong,
                            contentDescription = null
                        )
                    }
                },
                trailingIcon = {
                    if (form.isFineFilled) {
                        IconButton(
                            onClick = { onFineChange(null) }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Quitar multa"
                            )
                        }
                    }
                }
            )

            if (form.isFineFilled) {
                CardContent(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                ) {
                    Text(
                        text = "Motivo: ${form.fineReason.displayName}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium
                    )

                    if (form.fineReason == FineReason.OTHER && form.fineCustomReason.isNotEmpty()) {
                        Text(
                            text = "Motivo Personalizado: ${form.fineCustomReason}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    Text(
                        text = "Monto: ${form.fineAmount.toCurrencyString()}",
                        fontWeight = FontWeight.Medium
                    )
                    if (form.fineDueDate.isNotEmpty()) {
                        Text(
                            text = "Fecha vencimiento: ${form.fineDueDate}",
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }

        FormDropdown(
            label = "Método de pago",
            value = form.paymentMethod,
            options = PaymentMethod.entries,
            labelProvider = { it.displayName },
            onValueChange = { updateForm { copy(paymentMethod = it) } },
            isEnabled = !isReadOnly
        )

        FormDatePicker(
            modifier = Modifier.padding(bottom = 8.dp),
            value = form.paidAt.toDateString(),
            enabled = !isReadOnly,
            onDateSelected = { updateForm { copy(paidAt = it?:0L) } },
            label = "Fecha de pago",
            allowFutureDates = false
        )

        FormTextField(
            value = form.observation,
            onValueChange = { updateForm { copy(observation = it) } },
            label = "Observación",
            enabled = !isReadOnly,
            maxLength = 150,
            singleLine = false,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Description,
                    contentDescription = null
                )
            }
        )

        if (state.mode != PaymentFormMode.CREATE) {
            Spacer(Modifier.height(12.dp))
        }

        when (state.mode) {

            PaymentFormMode.CREATE,
            PaymentFormMode.EDIT -> {
                AppPrimaryButton(
                    text = state.mode.displayButton,
                    onClick = onSaveClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp)
                )
            }

            PaymentFormMode.READ_ONLY -> Unit
        }
    }
}