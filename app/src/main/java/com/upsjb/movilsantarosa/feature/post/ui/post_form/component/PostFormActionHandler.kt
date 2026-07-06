package com.upsjb.movilsantarosa.feature.post.ui.post_form.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.upsjb.movilsantarosa.core.uicomponents.MessageDialog
import com.upsjb.movilsantarosa.feature.post.ui.post_form.PostFormActionState

@Composable
fun PostFormActionHandler(
    action: PostFormActionState,
    onSuccess: () -> Unit,
    onReset: () -> Unit
) {
    LaunchedEffect(action) {
        if (action is PostFormActionState.Success) {
            onSuccess()
        }
    }

    if (action is PostFormActionState.Error) {
        MessageDialog(
            title = "Aviso",
            message = action.message,
            confirmButtonText = "Aceptar",
            onConfirmClick = onReset,
            onDismiss = onReset
        )
    }
}