
package com.upsjb.movilsantarosa.ui.feature.register

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.upsjb.movilsantarosa.domain.authentic.request.RolUser
import com.upsjb.movilsantarosa.ui.feature.register.components.ContactInfoSection
import com.upsjb.movilsantarosa.ui.feature.register.components.PersonalInfoSection
import com.upsjb.movilsantarosa.ui.feature.register.components.RegisterActions
import com.upsjb.movilsantarosa.ui.feature.register.components.SecurityInfoSection
import com.upsjb.movilsantarosa.ui.feature.register.components.VehicleInfoSection

@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel = viewModel(),
    onNavigateToLogin: () -> Unit = {}
) {
    val state = viewModel.state
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Logo o título
            Text(
                text = "Crear Nueva Cuenta",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Secciones del formulario
            PersonalInfoSection(
                firstName = state.firstName,
                lastName = state.lastName,
                dniNumber = state.dniNumber,
                birthdate = state.birthdate,
                onFirstNameChange = viewModel::updateFirstName,
                onLastNameChange = viewModel::updateLastName,
                onDniChange = viewModel::updateDni,
                onBirthdateChange = viewModel::updateBirthdate,
            )

            ContactInfoSection(
                email = state.email,
                phone = state.phone,
                onEmailChange = viewModel::updateEmail,
                onPhoneChange = viewModel::updatePhone,
            )

            VehicleInfoSection(
                plateNumber = state.plateNumber,
                vehicleColor = state.vehicleColor,
                licenceNumber = state.licenceNumber,
                onPlateChange = viewModel::updatePlateNumber,
                onColorChange = viewModel::updateVehicleColor,
                onLicenceChange = viewModel::updateLicenceNumber,
            )

            SecurityInfoSection(
                password = state.password,
                confirmPassword = state.confirmPassword,
                rolUser = state.rolUserDisplayName,
                onPasswordChange = viewModel::updatePassword,
                onConfirmPasswordChange = viewModel::updateConfirmPassword,
                onRolChange = { displayName ->
                    val rol = RolUser.values().find { it.displayName == displayName }
                    rol?.let { viewModel.updateRolUser(it) }
                },
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (state.isLoading) {
                CircularProgressIndicator()
                Spacer(modifier = Modifier.height(16.dp))
            }

            RegisterActions(
                onRegisterClick = {
                    if (viewModel.validateForm()) {
                        viewModel.register()
                    }
                },
                onLoginClick = onNavigateToLogin,
                isLoading = state.isLoading
            )

            Spacer(modifier = Modifier.height(16.dp))
        }

    if (showErrorDialog) {
        AlertDialog(
            onDismissRequest = { showErrorDialog = false },
            confirmButton = {
                TextButton(onClick = { showErrorDialog = false }) {
                    Text("Aceptar")
                }
            },
            title = { Text("Error") },
            text = { Text(errorMessage) }
        )
    }
}

@Preview(showBackground = true, name = "Full Register Screen")
@Composable
fun PreviewFullRegisterScreen() {
    MaterialTheme {
        RegisterScreen(
            onNavigateToLogin = {}
        )
    }
}

