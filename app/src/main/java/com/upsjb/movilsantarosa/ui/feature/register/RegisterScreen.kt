// ui/feature/register/RegisterScreen.kt
package com.upsjb.movilsantarosa.ui.feature.register

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.upsjb.movilsantarosa.domain.authentic.request.RolUser
import com.upsjb.movilsantarosa.ui.feature.register.components.ContactInfoSection
import com.upsjb.movilsantarosa.ui.feature.register.components.PersonalInfoSection
import com.upsjb.movilsantarosa.ui.feature.register.components.RegisterActions
import com.upsjb.movilsantarosa.ui.feature.register.components.SecurityInfoSection
import com.upsjb.movilsantarosa.ui.feature.register.components.VehicleInfoSection

@Composable
fun RegisterScreen(
    formState: RegisterFormState,
    onFirstNameChange: (String) -> Unit,
    onLastNameChange: (String) -> Unit,
    onDniChange: (String) -> Unit,
    onBirthdateChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onPlateChange: (String) -> Unit,
    onColorChange: (String) -> Unit,
    onLicenceChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onRolChange: (String) -> Unit,
    onRolUpdate: (RolUser) -> Unit,
    onRegisterClick: () -> Unit,
    onLoginClick: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Crear Nueva Cuenta",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(16.dp))

        PersonalInfoSection(
            firstName = formState.firstName,
            lastName = formState.lastName,
            dniNumber = formState.dniNumber,
            birthdate = formState.birthdate,
            onFirstNameChange = onFirstNameChange,
            onLastNameChange = onLastNameChange,
            onDniChange = onDniChange,
            onBirthdateChange = onBirthdateChange
        )

        ContactInfoSection(
            email = formState.email,
            phone = formState.phone,
            onEmailChange = onEmailChange,
            onPhoneChange = onPhoneChange
        )

        VehicleInfoSection(
            plateNumber = formState.plateNumber,
            vehicleColor = formState.vehicleColor,
            licenceNumber = formState.licenceNumber,
            onPlateChange = onPlateChange,
            onColorChange = onColorChange,
            onLicenceChange = onLicenceChange
        )

        SecurityInfoSection(
            password = formState.password,
            confirmPassword = formState.confirmPassword,
            rolUser = formState.rolUserDisplayName,
            onPasswordChange = onPasswordChange,
            onConfirmPasswordChange = onConfirmPasswordChange,
            onRolChange = { displayName ->
                val rol = RolUser.values().find { it.displayName == displayName }
                rol?.let { onRolUpdate(it) }
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        RegisterActions(
            onRegisterClick = onRegisterClick,
            onLoginClick = onLoginClick,
            isLoading = isLoading
        )

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewRegisterScreen() {
    RegisterScreen(
        formState = RegisterFormState(),
        onFirstNameChange = {},
        onLastNameChange = {},
        onDniChange = {},
        onBirthdateChange = {},
        onEmailChange = {},
        onPhoneChange = {},
        onPlateChange = {},
        onColorChange = {},
        onLicenceChange = {},
        onPasswordChange = {},
        onConfirmPasswordChange = {},
        onRolChange = {},
        onRolUpdate = {},
        onRegisterClick = {},
        onLoginClick = {}
    )
}