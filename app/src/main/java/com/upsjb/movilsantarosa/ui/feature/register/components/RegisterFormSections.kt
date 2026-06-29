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
import com.upsjb.movilsantarosa.ui.common.components.FormDatePicker
import com.upsjb.movilsantarosa.ui.common.components.FormSection
import com.upsjb.movilsantarosa.ui.common.components.FormTextField
import com.upsjb.movilsantarosa.domain.authentic.request.RolUser
import com.upsjb.movilsantarosa.ui.common.components.FormDropdown

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
    FormSection(title = "Información Personal") {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            FormTextField(
                value = firstName,
                onValueChange = onFirstNameChange,
                label = "Nombres",
                placeholder = "Ingresa tus nombres",
            )

            FormTextField(
                value = lastName,
                onValueChange = onLastNameChange,
                label = "Apellidos",
                placeholder = "Ingresa tus apellidos",
            )

            FormTextField(
                value = dniNumber,
                onValueChange = onDniChange,
                label = "DNI",
                placeholder = "Ingresa tu número de DNI",
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            )

            FormDatePicker(
                value = birthdate,
                onValueChange = onBirthdateChange,
                label = "Fecha de Nacimiento",
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
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            FormTextField(
                value = email,
                onValueChange = onEmailChange,
                label = "Correo Electrónico",
                placeholder = "correo@ejemplo.com",
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next,
            )

            FormTextField(
                value = phone,
                onValueChange = onPhoneChange,
                label = "Teléfono",
                placeholder = "Ingresa tu número de teléfono",
                keyboardType = KeyboardType.Phone,
                imeAction = ImeAction.Next,
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
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        FormSection(title = "Información del Vehículo") {
            FormTextField(
                value = plateNumber,
                onValueChange = onPlateChange,
                label = "Placa",
                placeholder = "ABC-123",
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next,
            )

            FormTextField(
                value = vehicleColor,
                onValueChange = onColorChange,
                label = "Color del Vehículo",
                placeholder = "Rojo, Azul, etc.",
            )

            FormTextField(
                value = licenceNumber,
                onValueChange = onLicenceChange,
                label = "Número de Licencia",
                placeholder = "Ingresa tu número de licencia",
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
    confirmPassword: String,
    rolUser: String,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onRolChange: (String) -> Unit,
) {
    val rolOptions = RolUser.values().map { it.displayName }

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        FormSection(title = "Seguridad") {
            FormTextField(
                value = password,
                onValueChange = onPasswordChange,
                label = "Contraseña",
                placeholder = "••••••••",
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next,
            )

            FormTextField(
                value = confirmPassword,
                onValueChange = onConfirmPasswordChange,
                label = "Confirmar Contraseña",
                placeholder = "••••••••",
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next,
            )

            FormDropdown(
                value = rolUser,
                onValueChange = onRolChange,
                label = "Tipo de Rol",
                options = rolOptions,
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
            var confirmPassword by remember { mutableStateOf("") }
            var rol by remember { mutableStateOf("Socio") }

            Box(modifier = Modifier.padding(16.dp)) {
                SecurityInfoSection(
                    password = password,
                    confirmPassword = confirmPassword,
                    rolUser = rol,
                    onPasswordChange = { password = it },
                    onConfirmPasswordChange = { confirmPassword = it },
                    onRolChange = { rol = it }
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
        Button(
            onClick = onRegisterClick,
            enabled = !isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (isLoading) "Registrando..." else "Registrarse")
        }

        Spacer(Modifier.width(12.dp))

        OutlinedButton(
            onClick = onLoginClick,
            enabled = !isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("¿Ya tienes cuenta? Iniciar Sesión")
        }
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
