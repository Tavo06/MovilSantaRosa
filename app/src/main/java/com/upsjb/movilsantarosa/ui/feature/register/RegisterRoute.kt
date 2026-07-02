package com.upsjb.movilsantarosa.ui.feature.register

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.upsjb.movilsantarosa.ui.common.components.MessageDialog
import com.upsjb.movilsantarosa.ui.common.components.ProgressIndicator

@Composable
fun RegisterRoute(
    onLoginNavigate: () -> Unit,
    viewModel: RegisterViewModel = hiltViewModel()
) {

    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier.safeDrawingPadding()
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            RegisterScreen(
                modifier = Modifier.fillMaxSize(),

                formState = state.form,

                onFormChange = viewModel::updateForm,

                onRegisterClick = viewModel::register,

                onLoginClick = onLoginNavigate,

                isLoading = state.uiState is RegisterActionUiState.Loading
            )

            when (val action = state.uiState) {

                RegisterActionUiState.Idle -> Unit

                RegisterActionUiState.Loading -> {
                    ProgressIndicator()
                }

                is RegisterActionUiState.Error -> {
                    MessageDialog(
                        title = "Aviso",
                        textButtonAccept = "Aceptar",
                        message = action.message,
                        onAccept = viewModel::reset
                    )
                }

                is RegisterActionUiState.Success -> {
                    onLoginNavigate()
                }
            }
        }
    }
}