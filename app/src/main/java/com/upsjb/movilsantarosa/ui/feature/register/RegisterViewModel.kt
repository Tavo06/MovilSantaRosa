
package com.upsjb.movilsantarosa.ui.feature.register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upsjb.movilsantarosa.domain.authentic.request.RegisterRequest
import com.upsjb.movilsantarosa.domain.authentic.request.RolUser
import kotlinx.coroutines.launch
import java.util.Locale

data class RegisterState(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val dniNumber: String = "",
    val birthdate: String = "",
    val phone: String = "",
    val plateNumber: String = "",
    val licenceNumber: String = "",
    val vehicleColor: String = "",
    val rolUser: RolUser = RolUser.PARTNER,
    val rolUserDisplayName: String = RolUser.PARTNER.displayName,
    val isLoading: Boolean = false,
    val errors: Map<String, String> = emptyMap()
)

class RegisterViewModel : ViewModel() {
    var state by mutableStateOf(RegisterState())
        private set

    fun updateEmail(value: String) {
        state = state.copy(email = value)
    }

    fun updatePassword(value: String) {
        state = state.copy(password = value)
    }

    fun updateConfirmPassword(value: String) {
        state = state.copy(confirmPassword = value)
    }

    fun updateFirstName(value: String) {
        state = state.copy(firstName = value)
    }

    fun updateLastName(value: String) {
        state = state.copy(lastName = value)
    }

    fun updateDni(value: String) {
        if (value.length <= 8) {
            state = state.copy(dniNumber = value)
        }
    }

    fun updateBirthdate(value: String) {
        state = state.copy(birthdate = value)
    }

    fun updatePhone(value: String) {
        state = state.copy(phone = value)
    }

    fun updatePlateNumber(value: String) {
        state = state.copy(plateNumber = value.uppercase(Locale.getDefault()))
    }

    fun updateLicenceNumber(value: String) {
        state = state.copy(licenceNumber = value)
    }

    fun updateVehicleColor(value: String) {
        state = state.copy(vehicleColor = value)
    }

    fun updateRolUser(rol: RolUser) {
        state = state.copy(
            rolUser = rol,
            rolUserDisplayName = rol.displayName
        )
    }

    fun validateForm(): Boolean {
        val errors = mutableMapOf<String, String>()

        if (state.firstName.isBlank()) {
            errors["firstname"] = "Los nombres son requeridos"
        }

        if (state.lastName.isBlank()) {
            errors["lastname"] = "Los apellidos son requeridos"
        }

        if (state.dniNumber.length != 8) {
            errors["dniNumber"] = "El DNI debe tener 8 dígitos"
        }

        if (state.birthdate.isBlank()) {
            errors["birthdate"] = "La fecha de nacimiento es requerida"
        }

        if (state.email.isBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(state.email).matches()) {
            errors["email"] = "Ingresa un correo válido"
        }

        if (state.phone.isBlank() || state.phone.length < 9) {
            errors["phone"] = "Ingresa un número de teléfono válido"
        }

        if (state.plateNumber.length < 6) {
            errors["plateNumber"] = "Ingresa una placa válida"
        }

        if (state.vehicleColor.isBlank()) {
            errors["vehicleColor"] = "El color del vehículo es requerido"
        }

        if (state.licenceNumber.isBlank()) {
            errors["licenceNumber"] = "El número de licencia es requerido"
        }

        if (state.password.length < 6) {
            errors["password"] = "La contraseña debe tener al menos 6 caracteres"
        }

        if (state.password != state.confirmPassword) {
            errors["confirmPassword"] = "Las contraseñas no coinciden"
        }

        state = state.copy(errors = errors)
        return errors.isEmpty()
    }

    fun register() {
        viewModelScope.launch {
            state = state.copy(isLoading = true)

            try {
                val request = RegisterRequest(
                    email = state.email,
                    password = state.password,
                    firstname = state.firstName,
                    lastname = state.lastName,
                    dniNumber = state.dniNumber,
                    birthdate = state.birthdate,
                    phone = state.phone,
                    plateNumber = state.plateNumber,
                    licenceNumber = state.licenceNumber,
                    vehicleColor = state.vehicleColor,
                    rolUser = state.rolUser
                )

                // Aquí iría la llamada a la API
                // val response = registerUseCase.execute(request)

                // Simulación de registro exitoso
                // Si falla, mostrar error
                state = state.copy(
                    isLoading = false,
                    errors = mapOf("general" to "Registro exitoso")
                )

            } catch (e: Exception) {

            }
        }
    }
}