package com.upsjb.movilsantarosa.feature.auth.ui.register

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.upsjb.movilsantarosa.core.uicomponents.AppHeader
import com.upsjb.movilsantarosa.core.uicomponents.MessageDialog
import com.upsjb.movilsantarosa.core.uicomponents.ProgressIndicatorOverlay
import com.upsjb.movilsantarosa.feature.auth.ui.register.components.ContactInfoSection
import com.upsjb.movilsantarosa.feature.auth.ui.register.components.PersonalInfoSection
import com.upsjb.movilsantarosa.feature.auth.ui.register.components.RegisterActions
import com.upsjb.movilsantarosa.feature.auth.ui.register.components.SecurityInfoSection
import com.upsjb.movilsantarosa.feature.auth.ui.register.components.VehicleInfoSection

@Composable
fun RegisterScreen(
    navigateToLogin: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: RegisterViewModel = hiltViewModel()
) {

    val state by viewModel.uiState.collectAsStateWithLifecycle()

    RegisterActionHandler(
        action = state.uiState,
        onLogin = navigateToLogin,
        onReset = viewModel::reset
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {

        RegisterContent(
            modifier = Modifier
                .safeDrawingPadding(),
            state = state,
            updateForm = viewModel::updateForm,
            onRegisterClick = viewModel::register,
            onLoginClick = navigateToLogin
        )

        if (state.uiState is RegisterActionUiState.Loading) {
            ProgressIndicatorOverlay()
        }
    }
}

@Composable
private fun RegisterActionHandler(
    action: RegisterActionUiState,
    onLogin: () -> Unit,
    onReset: () -> Unit
) {

    LaunchedEffect(action) {
        if (action is RegisterActionUiState.Success) {
            onLogin()
        }
    }

    if (action is RegisterActionUiState.Error) {
        MessageDialog(
            title = "Aviso",
            message = action.message,
            confirmButtonText = "Aceptar",
            onConfirmClick = onReset,
            onDismiss = {}
        )
    }
}

@Composable
private fun RegisterContent(
    modifier: Modifier = Modifier,
    state: RegisterUiState,
    updateForm: (RegisterFormState.() -> RegisterFormState) -> Unit,
    onRegisterClick: () -> Unit,
    onLoginClick: () -> Unit
) {

    val form = state.form

    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        AppHeader(
            title = "Crear Cuenta",
            onBackClick = onLoginClick
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {

            PersonalInfoSection(
                firstName = form.firstName,
                lastName = form.lastName,
                dniNumber = form.dniNumber,
                birthdate = form.birthDate,

                onFirstNameChange = {
                    updateForm {
                        copy(firstName = it)
                    }
                },

                onLastNameChange = {
                    updateForm {
                        copy(lastName = it)
                    }
                },

                onDniChange = {
                    updateForm {
                        copy(dniNumber = it.take(8))
                    }
                },

                onBirthdateChange = {
                    updateForm {
                        copy(birthDate = it)
                    }
                }
            )

            ContactInfoSection(
                email = form.email,
                phone = form.phone,

                onEmailChange = {
                    updateForm {
                        copy(email = it)
                    }
                },

                onPhoneChange = {
                    updateForm {
                        copy(phone = it)
                    }
                }
            )

            VehicleInfoSection(
                plateNumber = form.plateNumber,
                vehicleColor = form.vehicleColor,
                licenceNumber = form.licenceNumber,

                onPlateChange = {
                    updateForm {
                        copy(
                            plateNumber = it.uppercase()
                        )
                    }
                },

                onColorChange = {
                    updateForm {
                        copy(vehicleColor = it)
                    }
                },

                onLicenceChange = {
                    updateForm {
                        copy(licenceNumber = it)
                    }
                }
            )

            SecurityInfoSection(
                password = form.password,
                onPasswordChange = {
                    updateForm {
                        copy(password = it)
                    }
                }
            )

            RegisterActions(
                onRegisterClick = onRegisterClick,
                onLoginClick = onLoginClick,
                isLoading = state.uiState is RegisterActionUiState.Loading
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun RegisterContentPreview() {

    RegisterContent(
        state = RegisterUiState(),
        updateForm = {},
        onRegisterClick = {},
        onLoginClick = {}
    )
}