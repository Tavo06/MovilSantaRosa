package com.upsjb.movilsantarosa.feature.fine.ui.fine_form.component

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
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.PersonSearch
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.upsjb.movilsantarosa.core.uicomponents.AppHeader
import com.upsjb.movilsantarosa.core.uicomponents.AppPrimaryButton
import com.upsjb.movilsantarosa.core.uicomponents.CardContent
import com.upsjb.movilsantarosa.core.uicomponents.FormDatePicker
import com.upsjb.movilsantarosa.core.uicomponents.FormDropdown
import com.upsjb.movilsantarosa.core.uicomponents.FormTextField
import com.upsjb.movilsantarosa.core.utils.toDateString
import com.upsjb.movilsantarosa.feature.auth.domain.model.UserRole
import com.upsjb.movilsantarosa.feature.fine.data.model.FineReason
import com.upsjb.movilsantarosa.feature.fine.data.model.FineStatus
import com.upsjb.movilsantarosa.feature.fine.ui.fine_form.FineFormMode
import com.upsjb.movilsantarosa.feature.fine.ui.fine_form.FineFormState
import com.upsjb.movilsantarosa.feature.fine.ui.fine_form.FineFormUiState
import com.upsjb.movilsantarosa.feature.members.domain.model.Member

@Composable
fun FineFormContent(
    modifier: Modifier = Modifier,
    state: FineFormUiState,
    onMemberChange: (Member?) -> Unit,
    updateForm: (FineFormState.() -> FineFormState) -> Unit,
    onSaveClick: () -> Unit,
    onEditClick: () -> Unit,
    onBackClick: () -> Unit,
    openMemberPicker: () -> Unit,
) {
    val form = state.form
    val isReadOnly = state.mode == FineFormMode.READ_ONLY
    val isCreateMode = state.mode == FineFormMode.CREATE

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
        }

        FormDropdown(
            label = "Motivo",
            value = form.reason,
            options = FineReason.entries,
            labelProvider = { it.displayName },
            onValueChange = { updateForm { copy(reason = it) } },
            isEnabled = !isReadOnly
        )
        Spacer(Modifier.height(4.dp))

        if (form.reason == FineReason.OTHER) {
            FormTextField(
                value = form.customReason,
                onValueChange = { updateForm { copy(customReason = it) } },
                label = "Motivo personalizado",
                enabled = !isReadOnly
            )
        }

        FormTextField(
            value = form.amount,
            onValueChange = {
                if (it.all { char -> char.isDigit() }) {
                    updateForm { copy(amount = it) }
                }
            },
            label = "Monto",
            maxLength = 3,
            enabled = !isReadOnly,
            keyboardType = KeyboardType.Number,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.AttachMoney,
                    contentDescription = null
                )
            }
        )

        FormTextField(
            value = form.description,
            onValueChange = { updateForm { copy(description = it) } },
            label = "Descripción",
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
        FormDatePicker(
            modifier = Modifier.padding(bottom = 8.dp),
            value = form.issuedAt.toDateString(),
            enabled = !isReadOnly,
            onDateSelected = { updateForm { copy(issuedAt = it ?: 0L) } },
            label = "Fecha de imposición",
            allowFutureDates = false
        )

        if (!isCreateMode) {
            FormDropdown(
                modifier = Modifier.padding(bottom = 24.dp),
                label = "Estado Multa",
                value = form.status,
                options = FineStatus.entries,
                labelProvider = { it.displayName },
                onValueChange = { updateForm { copy(status = it) } },
                isEnabled = !isReadOnly
            )
        }

        when (state.mode) {

            FineFormMode.CREATE,
            FineFormMode.EDIT -> {
                AppPrimaryButton(
                    text = state.mode.displayButton,
                    onClick = onSaveClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp)
                )
            }

            FineFormMode.READ_ONLY -> Unit
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun FineFormContentPreview() {

    var state by remember {
        mutableStateOf(
            FineFormUiState(
                form = FineFormState(
                    memberName = "Juan Pérez",
                    memberEmail = "juan@mail.com",
                    reason = FineReason.OTHER,
                    customReason = "Falta de respeto",
                    amount = "50.00",
                    description = "Incidente en reunión"
                ),
                mode = FineFormMode.CREATE
            )
        )
    }

    MaterialTheme {
        Surface {
            FineFormContent(
                state = state,
                updateForm = { transform ->
                    state = state.copy(
                        form = state.form.transform()
                    )
                },
                onSaveClick = {
                    state = state.copy(
                        mode = FineFormMode.READ_ONLY
                    )
                },
                onEditClick = {

                },
                onBackClick = {},
                openMemberPicker = {},
                onMemberChange = {}
            )
        }
    }
}