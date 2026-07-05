package com.upsjb.movilsantarosa.feature.fine.ui.fine_form.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.upsjb.movilsantarosa.core.uicomponents.MessageDialog
import com.upsjb.movilsantarosa.feature.fine.ui.fine_form.FineFormActionState

@Composable
fun FineFormActionHandler(
    action: FineFormActionState,
    onReset: () -> Unit
) {

    if (action is FineFormActionState.Error) {
        MessageDialog(
            title = "Error",
            message = action.message,
            confirmButtonText = "Aceptar",
            onConfirmClick = onReset,
            onDismiss = onReset
        )
    }
}

@Preview(showBackground = true)
@Composable
fun FineFormActionHandlerPreview() {

    var state by remember {
        mutableStateOf<FineFormActionState>(
            FineFormActionState.Error("No se pudo guardar la multa")
        )
    }

    MaterialTheme {
        Box(modifier = Modifier.fillMaxSize()) {

            FineFormActionHandler(
                action = state,
                onReset = {
                    state = FineFormActionState.Idle
                }
            )
        }
    }
}