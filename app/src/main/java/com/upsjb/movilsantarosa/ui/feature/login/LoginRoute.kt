package com.upsjb.movilsantarosa.ui.feature.login

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
import com.upsjb.movilsantarosa.ui.feature.login.components.ErrorDialog
import com.upsjb.movilsantarosa.ui.feature.login.components.ProgressIndicatorOverlay

@Composable
fun LoginRoute(
    onLoginSuccess: () -> Unit,
    onRegisterClick: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    Scaffold(
        modifier = Modifier.safeDrawingPadding()
    ) { padding ->
        Box(
            modifier = Modifier.fillMaxSize().padding(padding)) {

            LoginScreen(
                uiState = uiState,

                onLoginClick = { email, password ->
                    viewModel.login(email, password)
                },

                onRegisterClick = onRegisterClick,

                modifier = Modifier.fillMaxSize()
            )

            when (uiState) {

                is LoginUIState.Error -> {

                    ErrorDialog(
                        message = (uiState as LoginUIState.Error).message,
                        onAccept = viewModel::reset
                    )
                }

                is LoginUIState.Success -> {
                    onLoginSuccess()
                }

                else -> Unit
            }
        }

    }
}