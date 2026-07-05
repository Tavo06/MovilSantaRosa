package com.upsjb.movilsantarosa.feature.auth.ui.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.upsjb.movilsantarosa.core.uicomponents.MessageDialog
import com.upsjb.movilsantarosa.feature.auth.ui.login.components.LoginIdleContent
import com.upsjb.movilsantarosa.core.uicomponents.ProgressIndicatorOverlay

@Composable
fun LoginScreen(
    onRegisterClick: () -> Unit,
    onLoginSuccess: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(uiState) {
        if (uiState is LoginUIState.Success) {
            onLoginSuccess()
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        LoginIdleContent(
            modifier = Modifier
                .safeDrawingPadding(),
            email = email,
            password = password,
            onEmailChange = { email = it },
            onPasswordChange = { password = it },
            onLoginClick = {
                viewModel.login(email, password)
            },
            onRegisterClick = onRegisterClick,
        )

        if (uiState is LoginUIState.Loading) {
            ProgressIndicatorOverlay()
        }

        if (uiState is LoginUIState.Error) {
            MessageDialog(
                title = "Aviso",
                message = (uiState as LoginUIState.Error).message,
                onConfirmClick = viewModel::reset,
                onDismiss = {},
                confirmButtonText = "Aceptar"
            )
        }
    }
}