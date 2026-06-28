package com.upsjb.movilsantarosa.ui.feature.login

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.upsjb.movilsantarosa.ui.feature.login.components.LoginIdleContent
import com.upsjb.movilsantarosa.ui.feature.login.components.ProgressIndicatorOverlay

@Composable
fun LoginScreen(
    uiState: LoginUIState,
    onLoginClick: (email: String, password: String) -> Unit,
    onRegisterClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    Box(modifier = modifier.fillMaxSize()) {

        LoginIdleContent(
            email = email,
            password = password,
            onEmailChange = { email = it },
            onPasswordChange = { password = it },
            onLoginClick = {
                onLoginClick(email, password)
            },
            onRegisterClick = onRegisterClick,
            modifier = Modifier.fillMaxSize()
        )

        when (uiState) {
            is LoginUIState.Loading -> {
                ProgressIndicatorOverlay()
            }

            is LoginUIState.Idle -> Unit

            is LoginUIState.Success -> Unit

            is LoginUIState.Error -> Unit
        }
    }
}