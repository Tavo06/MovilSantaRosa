package com.upsjb.movilsantarosa.feature.fine.ui.fine_form

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.upsjb.movilsantarosa.core.uicomponents.ProgressIndicatorOverlay
import com.upsjb.movilsantarosa.feature.fine.ui.fine_form.component.FineFormActionHandler
import com.upsjb.movilsantarosa.feature.fine.ui.fine_form.component.FineFormContent

@Composable
fun FineFormScreen(
    modifier: Modifier = Modifier,
    viewModel: FineFormViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    FineFormActionHandler(
        action = state.actionState,
        onReset = viewModel::resetAction
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        FineFormContent(
            state = state,
            updateForm = viewModel::updateForm,
            onSave = viewModel::saveFine
        )

        if (state.actionState is FineFormActionState.Loading) {
            ProgressIndicatorOverlay()
        }
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
        state = state,
        updateForm = {},
        onSave = {}
    )
}