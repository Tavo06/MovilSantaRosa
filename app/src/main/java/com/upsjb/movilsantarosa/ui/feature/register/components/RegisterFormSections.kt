package com.upsjb.movilsantarosa.ui.feature.register.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.upsjb.movilsantarosa.ui.common.components.AppOutlinedButton
import com.upsjb.movilsantarosa.ui.common.components.AppPrimaryButton
import com.upsjb.movilsantarosa.ui.common.components.FormDatePicker
import com.upsjb.movilsantarosa.ui.common.components.FormSection
import com.upsjb.movilsantarosa.ui.common.components.FormTextField
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun PersonalInfoSection(
    firstName: String,
    lastName: String,
    dniNumber: String,
    birthdate: String,
    onFirstNameChange: (String) -> Unit,
    onLastNameChange: (String) -> Unit,
    onDniChange: (String) -> Unit,
    onBirthdateChange: (String) -> Unit,
) {
    val formatter = remember {
        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    }
    FormSection(title = "Información Personal") {
        Column {
            FormTextField(
                value = firstName,
                onValueChange = onFirstNameChange,
                label = "Nombres",
                placeholder = "Ingresa tus nombres",
                maxLength = 50,
                singleLine = true,
            )

            FormTextField(
                value = lastName,
                onValueChange = onLastNameChange,
                label = "Apellidos",
                placeholder = "Ingresa tus apellidos",
                maxLength = 50,
                singleLine = true,
            )

            FormTextField(
                value = dniNumber,
                onValueChange = onDniChange,
                label = "DNI",
                placeholder = "Ingresa tu número de DNI",
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next,
                maxLength = 8,
                singleLine = true,
            )

            FormDatePicker(
                value = birthdate,
                label = "Fecha de nacimiento",
                onDateSelected = { millis ->
                    millis?.let {
                        onBirthdateChange(formatter.format(Date(it)))
                    }
                }
            )
        }

    }
}

@Preview(showBackground = true, name = "Personal Info Section")
@Composable
fun PreviewPersonalInfoSection() {
    MaterialTheme {
        Surface {
            var firstName by remember { mutableStateOf("") }
            var lastName by remember { mutableStateOf("") }
            var dni by remember { mutableStateOf("") }
            var birthdate by remember { mutableStateOf("") }

            Box(modifier = Modifier.padding(16.dp)) {
                PersonalInfoSection(
                    firstName = firstName,
                    lastName = lastName,
                    dniNumber = dni,
                    birthdate = birthdate,
                    onFirstNameChange = { firstName = it },
                    onLastNameChange = { lastName = it },
                    onDniChange = { dni = it },
                    onBirthdateChange = { birthdate = it }
                )
            }
        }
    }
}

@Composable
fun ContactInfoSection(
    email: String,
    phone: String,
    onEmailChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
) {
    FormSection(title = "Información de Contacto") {
        Column {
            FormTextField(
                value = email,
                onValueChange = onEmailChange,
                label = "Correo Electrónico",
                placeholder = "correo@ejemplo.com",
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next,
                maxLength = 50,
                singleLine = true,
            )

            FormTextField(
                value = phone,
                onValueChange = onPhoneChange,
                label = "Número Celular",
                placeholder = "Ingresa tu número de celular",
                keyboardType = KeyboardType.Phone,
                imeAction = ImeAction.Next,
                maxLength = 9,
                singleLine = true,
            )
        }

    }
}

@Preview(showBackground = true, name = "Contact Info Section")
@Composable
fun PreviewContactInfoSection() {
    MaterialTheme {
        Surface {
            var email by remember { mutableStateOf("") }
            var phone by remember { mutableStateOf("") }

            Box(modifier = Modifier.padding(16.dp)) {
                ContactInfoSection(
                    email = email,
                    phone = phone,
                    onEmailChange = { email = it },
                    onPhoneChange = { phone = it }
                )
            }
        }
    }
}

@Composable
fun VehicleInfoSection(
    plateNumber: String,
    vehicleColor: String,
    licenceNumber: String,
    onPlateChange: (String) -> Unit,
    onColorChange: (String) -> Unit,
    onLicenceChange: (String) -> Unit,
) {
    Column {
        FormSection(title = "Información del Vehículo") {
            FormTextField(
                value = plateNumber,
                onValueChange = onPlateChange,
                label = "Placa",
                placeholder = "ABC123",
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next,
                maxLength = 6,
                singleLine = true,
            )

            FormTextField(
                value = vehicleColor,
                onValueChange = onColorChange,
                label = "Color del Vehículo",
                placeholder = "Rojo, Azul, etc.",
                maxLength = 20,
                singleLine = true,
            )

            FormTextField(
                value = licenceNumber,
                onValueChange = onLicenceChange,
                label = "Número de Licencia",
                placeholder = "Ingresa tu número de licencia",
                maxLength = 12,
                singleLine = true,
            )
        }
    }
}

@Preview(showBackground = true, name = "Vehicle Info Section")
@Composable
fun PreviewVehicleInfoSection() {
    MaterialTheme {
        Surface {
            var plate by remember { mutableStateOf("") }
            var color by remember { mutableStateOf("") }
            var licence by remember { mutableStateOf("") }

            Box(modifier = Modifier.padding(16.dp)) {
                VehicleInfoSection(
                    plateNumber = plate,
                    vehicleColor = color,
                    licenceNumber = licence,
                    onPlateChange = { plate = it },
                    onColorChange = { color = it },
                    onLicenceChange = { licence = it }
                )
            }
        }
    }
}

@Composable
fun SecurityInfoSection(
    password: String,
    onPasswordChange: (String) -> Unit,
) {

    Column {
        FormSection(title = "Seguridad") {
            FormTextField(
                value = password,
                onValueChange = onPasswordChange,
                label = "Contraseña",
                placeholder = "••••••••",
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next,
                maxLength = 20,
                singleLine = true,
                isPassword = true
            )
        }
    }
}

@Preview(showBackground = true, name = "Security Info Section")
@Composable
fun PreviewSecurityInfoSection() {
    MaterialTheme {
        Surface {
            var password by remember { mutableStateOf("") }

            Box(modifier = Modifier.padding(16.dp)) {
                SecurityInfoSection(
                    password = password,
                    onPasswordChange = { password = it },
                )
            }
        }
    }
}

@Composable
fun RegisterActions(
    onRegisterClick: () -> Unit,
    onLoginClick: () -> Unit,
    isLoading: Boolean = false,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        AppPrimaryButton(
            text = if (isLoading) "Registrando..." else "Registrarse",
            onClick = onRegisterClick,
            enabled = !isLoading,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.width(12.dp))

        AppOutlinedButton(
            text = "¿Ya tienes cuenta? Iniciar Sesión",
            onClick = onLoginClick,
            enabled = !isLoading,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(showBackground = true, name = "Register Actions")
@Composable
fun PreviewRegisterActions() {
    MaterialTheme {
        Surface {
            Box(modifier = Modifier.padding(16.dp)) {
                RegisterActions(
                    onRegisterClick = {},
                    onLoginClick = {}
                )
            }
        }
    }
}
