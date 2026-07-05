package com.upsjb.movilsantarosa.feature.fine.ui.fine_form.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.upsjb.movilsantarosa.core.uicomponents.AppPrimaryButton
import com.upsjb.movilsantarosa.core.uicomponents.FormDropdown
import com.upsjb.movilsantarosa.core.uicomponents.FormTextField
import com.upsjb.movilsantarosa.feature.fine.data.model.FineReason
import com.upsjb.movilsantarosa.feature.fine.ui.fine_form.FineFormMode
import com.upsjb.movilsantarosa.feature.fine.ui.fine_form.FineFormState
import com.upsjb.movilsantarosa.feature.fine.ui.fine_form.FineFormUiState

@Composable
fun FineFormContent(
    state: FineFormUiState,
    updateForm: (FineFormState.() -> FineFormState) -> Unit,
    onSave: () -> Unit
) {

    val form = state.form
    val isReadOnly = state.mode == FineFormMode.READ_ONLY

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState()),
    ) {

        Text(
            text = when (state.mode) {
                FineFormMode.CREATE -> "Registrar multa"
                FineFormMode.EDIT -> "Editar multa"
                FineFormMode.READ_ONLY -> "Detalle de multa"
            },
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        FormTextField(
            value = form.memberName,
            onValueChange = { updateForm { copy(memberName = it) } },
            label = "Nombre del socio",
            enabled = !isReadOnly
        )

        FormTextField(
            value = form.memberEmail,
            onValueChange = { updateForm { copy(memberEmail = it) } },
            label = "Correo",
            enabled = !isReadOnly
        )

        FormDropdown(
            label = "Motivo",
            value = form.reason,
            options = FineReason.entries,
            labelProvider = { it.displayName },
            onValueChange = { updateForm { copy(reason = it) } },
            isEnabled = !isReadOnly
        )

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
            onValueChange = { updateForm { copy(amount = it) } },
            label = "Monto",
            enabled = !isReadOnly
        )

        FormTextField(
            value = form.description,
            onValueChange = { updateForm { copy(description = it) } },
            label = "Descripción",
            enabled = !isReadOnly
        )

        if (!isReadOnly) {
            AppPrimaryButton(
                text = if (state.mode == FineFormMode.EDIT) "Actualizar" else "Guardar",
                onClick = onSave,
                modifier = Modifier.fillMaxWidth()
            )
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
                onSave = {
                    state = state.copy(
                        mode = FineFormMode.READ_ONLY
                    )
                }
            )
        }
    }
}