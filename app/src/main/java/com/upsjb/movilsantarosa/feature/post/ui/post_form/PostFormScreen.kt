package com.upsjb.movilsantarosa.feature.post.ui.post_form

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
import com.upsjb.movilsantarosa.feature.post.data.model.PostType
import com.upsjb.movilsantarosa.feature.post.data.model.Priority
import com.upsjb.movilsantarosa.feature.post.ui.post_form.component.PostFormActionHandler
import com.upsjb.movilsantarosa.feature.post.ui.post_form.component.PostFormContent

@Composable
fun PostFormScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    onSuccess: () -> Unit,
    openLocationPicker: () -> Unit,
    viewModel: PostFormViewModel = hiltViewModel(),
) {

    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var showSaveDialog by remember { mutableStateOf(false) }

    PostFormActionHandler(
        action = state.actionState,
        onReset = viewModel::resetAction,
        onSuccess = onSuccess
    )

    PostFormContent(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .fillMaxSize(),
        state = state,
        updateForm = viewModel::updateForm,
        onSaveClick = {
            showSaveDialog = true
        },
        onEditClick = {
            viewModel.setMode(PostFormMode.EDIT)
        },
        onBackClick = onBackClick,
        openLocationPicker = openLocationPicker
    )

    if (state.actionState is PostFormActionState.Loading) {
        ProgressIndicatorOverlay()
    }

    if (showSaveDialog) {
        MessageDialog(
            title = "Confirmar",
            message = "¿Desea ${state.mode.displayName.lowercase()} este post?",
            confirmButtonText = "Sí",
            cancelButtonText = "No, cancelar",
            onConfirmClick = {
                showSaveDialog = false
                viewModel.savePost()
            },
            onDismiss = {
                showSaveDialog = false
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PostFormScreenPreview() {

    val state = PostFormUiState(
        form = PostFormState(
            title = "Aviso importante",
            description = "Se realizará mantenimiento del sistema",
            type = PostType.ANNOUNCEMENT,
            priority = Priority.NORMAL,
            address = "Av. Principal 123",
            latitude = "-12.0453",
            longitude = "-77.0311",
            expiredAt = currentDateString()
        ),
        mode = PostFormMode.CREATE,
        postId = ""
    )

    MaterialTheme {
        PostFormContent(
            state = state,
            updateForm = {},
            onSaveClick = {},
            onEditClick = {},
            onBackClick = {},
            openLocationPicker = {}
        )
    }
}