// ui/feature/register/RegisterRoute.kt
package com.upsjb.movilsantarosa.ui.feature.register

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.upsjb.movilsantarosa.domain.authentic.request.RolUser
import com.upsjb.movilsantarosa.ui.feature.register.components.RegisterErrorDialog
import com.upsjb.movilsantarosa.ui.feature.register.components.RegisterProgressIndicator

@Composable
fun RegisterRoute(
    onLoginNavigate: () -> Unit,
    viewModel: RegisterViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val formState by viewModel.formState.collectAsStateWithLifecycle()

    Box(modifier = Modifier.fillMaxSize()) {
        RegisterScreen(
            formState = formState,
            onFirstNameChange = viewModel::updateFirstName,
            onLastNameChange = viewModel::updateLastName,
            onDniChange = viewModel::updateDni,
            onBirthdateChange = viewModel::updateBirthdate,
            onEmailChange = viewModel::updateEmail,
            onPhoneChange = viewModel::updatePhone,
            onPlateChange = viewModel::updatePlateNumber,
            onColorChange = viewModel::updateVehicleColor,
            onLicenceChange = viewModel::updateLicenceNumber,
            onPasswordChange = viewModel::updatePassword,
            onConfirmPasswordChange = viewModel::updateConfirmPassword,
            onRolChange = { displayName ->
                val rol = RolUser.values().find { it.displayName == displayName }
                rol?.let { viewModel.updateRolUser(it) }
            },
            onRolUpdate = viewModel::updateRolUser,
            onRegisterClick = viewModel::register,
            onLoginClick = onLoginNavigate,
            isLoading = uiState is RegisterUIState.Loading,
            modifier = Modifier.fillMaxSize()
        )

        when (uiState) {
            RegisterUIState.Loading -> RegisterProgressIndicator()
            is RegisterUIState.Error -> RegisterErrorDialog(
                message = (uiState as RegisterUIState.Error).message,
                onAccept = viewModel::reset
            )
            is RegisterUIState.Success -> onLoginNavigate()
            else -> Unit
        }
    }
}