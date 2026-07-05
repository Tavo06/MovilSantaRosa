package com.upsjb.movilsantarosa.feature.fine.ui.fine_form

import androidx.compose.foundation.layout.padding
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
import com.upsjb.movilsantarosa.feature.fine.ui.fine_form.component.FineFormActionHandler
import com.upsjb.movilsantarosa.feature.fine.ui.fine_form.component.FineFormContent

@Composable
fun FineFormScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    openMemberPicker: () -> Unit,
    viewModel: FineFormViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var showRegisterDialog by remember { mutableStateOf(false) }

    FineFormActionHandler(
        action = state.actionState,
        onReset = viewModel::resetAction,
        navigateToFine = onBackClick
    )

    FineFormContent(
        modifier = modifier.padding(horizontal = 16.dp),
        state = state,
        member = state.selectedMember,
        updateForm = viewModel::updateForm,
        onSave = {
            showRegisterDialog = true
        },
        onBackClick = onBackClick,
        openMemberPicker = openMemberPicker,
        onMemberChange = {
            if (it == null) {
                viewModel.clearMember()
            } else {
                viewModel.selectMember(it)
            }
        }
    )

    if (state.actionState is FineFormActionState.Loading) {
        ProgressIndicatorOverlay()
    }

    if (showRegisterDialog) {
        MessageDialog(
            title = "Confirmar",
            message = "¿Desea completar el registro de multa?",
            confirmButtonText = "Sí, registrar",
            onConfirmClick = {
                showRegisterDialog = false
                viewModel.saveFine()
            },
            onDismiss = {
                showRegisterDialog = false
            },
            cancelButtonText = "No, Cancelar"
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FineFormPreview() {

    val state = FineFormUiState(
        form = FineFormState(
            memberName = "Juan Pérez",
            memberEmail = "juan@mail.com",
            amount = "50"
        ),
        mode = FineFormMode.CREATE
    )

    FineFormContent(
        member = null,
        state = state,
        updateForm = {},
        onSave = {},
        onBackClick = {},
        openMemberPicker = {},
        onMemberChange = {}
    )
}