// ui/feature/register/RegisterViewModel.kt
package com.upsjb.movilsantarosa.ui.feature.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upsjb.movilsantarosa.domain.authentic.request.RegisterRequest
import com.upsjb.movilsantarosa.domain.authentic.request.RolUser
import com.upsjb.movilsantarosa.domain.authentic.usecase.RegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase  // Cambiado a minúscula por convención
) : ViewModel() {

    private val _uiState = MutableStateFlow<RegisterUIState>(RegisterUIState.Idle)
    val uiState: StateFlow<RegisterUIState> = _uiState.asStateFlow()

    private val _formState = MutableStateFlow(RegisterFormState())
    val formState: StateFlow<RegisterFormState> = _formState.asStateFlow()

    // Función genérica para actualizar cualquier campo
    fun updateField(
        email: String? = null,
        password: String? = null,
        confirmPassword: String? = null,
        firstName: String? = null,
        lastName: String? = null,
        dniNumber: String? = null,
        birthdate: String? = null,
        phone: String? = null,
        plateNumber: String? = null,
        licenceNumber: String? = null,
        vehicleColor: String? = null,
        rolUser: RolUser? = null
    ) {
        val current = _formState.value
        _formState.value = current.copy(
            email = email ?: current.email,
            password = password ?: current.password,
            confirmPassword = confirmPassword ?: current.confirmPassword,
            firstName = firstName ?: current.firstName,
            lastName = lastName ?: current.lastName,
            dniNumber = dniNumber ?: current.dniNumber,
            birthdate = birthdate ?: current.birthdate,
            phone = phone ?: current.phone,
            plateNumber = plateNumber ?: current.plateNumber,
            licenceNumber = licenceNumber ?: current.licenceNumber,
            vehicleColor = vehicleColor ?: current.vehicleColor,
            rolUser = rolUser ?: current.rolUser,
            rolUserDisplayName = (rolUser ?: current.rolUser).displayName
        )
    }

    // Función helper para actualizar un solo campo
    fun updateSingleField(
        email: String? = null,
        password: String? = null,
        confirmPassword: String? = null,
        firstName: String? = null,
        lastName: String? = null,
        dniNumber: String? = null,
        birthdate: String? = null,
        phone: String? = null,
        plateNumber: String? = null,
        licenceNumber: String? = null,
        vehicleColor: String? = null,
        rolUser: RolUser? = null
    ) {
        updateField(
            email = email,
            password = password,
            confirmPassword = confirmPassword,
            firstName = firstName,
            lastName = lastName,
            dniNumber = dniNumber,
            birthdate = birthdate,
            phone = phone,
            plateNumber = plateNumber,
            licenceNumber = licenceNumber,
            vehicleColor = vehicleColor,
            rolUser = rolUser
        )
    }

    // Funciones para actualizar campos específicos
    fun updateEmail(email: String) = updateSingleField(email = email)
    fun updatePassword(password: String) = updateSingleField(password = password)
    fun updateConfirmPassword(confirmPassword: String) = updateSingleField(confirmPassword = confirmPassword)
    fun updateFirstName(firstName: String) = updateSingleField(firstName = firstName)
    fun updateLastName(lastName: String) = updateSingleField(lastName = lastName)
    fun updateDni(dniNumber: String) {
        if (dniNumber.length <= 8) {
            updateSingleField(dniNumber = dniNumber)
        }
    }
    fun updateBirthdate(birthdate: String) = updateSingleField(birthdate = birthdate)
    fun updatePhone(phone: String) = updateSingleField(phone = phone)
    fun updatePlateNumber(plateNumber: String) {
        updateSingleField(plateNumber = plateNumber.uppercase(Locale.getDefault()))
    }
    fun updateLicenceNumber(licenceNumber: String) = updateSingleField(licenceNumber = licenceNumber)
    fun updateVehicleColor(vehicleColor: String) = updateSingleField(vehicleColor = vehicleColor)
    fun updateRolUser(rolUser: RolUser) = updateSingleField(rolUser = rolUser)

    fun validateForm(): Boolean {
        val current = _formState.value
        val errors = mutableMapOf<String, String>()

        if (current.firstName.isBlank()) {
            errors["firstname"] = "Los nombres son requeridos"
        }

        if (current.lastName.isBlank()) {
            errors["lastname"] = "Los apellidos son requeridos"
        }

        if (current.dniNumber.length != 8) {
            errors["dniNumber"] = "El DNI debe tener 8 dígitos"
        }

        if (current.birthdate.isBlank()) {
            errors["birthdate"] = "La fecha de nacimiento es requerida"
        }

        if (current.email.isBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(current.email).matches()) {
            errors["email"] = "Ingresa un correo válido"
        }

        if (current.phone.isBlank() || current.phone.length < 9) {
            errors["phone"] = "Ingresa un número de teléfono válido"
        }

        if (current.plateNumber.length < 6) {
            errors["plateNumber"] = "Ingresa una placa válida"
        }

        if (current.vehicleColor.isBlank()) {
            errors["vehicleColor"] = "El color del vehículo es requerido"
        }

        if (current.licenceNumber.isBlank()) {
            errors["licenceNumber"] = "El número de licencia es requerido"
        }

        if (current.password.length < 6) {
            errors["password"] = "La contraseña debe tener al menos 6 caracteres"
        }

        if (current.password != current.confirmPassword) {
            errors["confirmPassword"] = "Las contraseñas no coinciden"
        }

        _formState.value = current.copy(errors = errors)
        return errors.isEmpty()
    }

    fun register() {
        val current = _formState.value

        if (!validateForm()) {
            return
        }

        viewModelScope.launch {
            _uiState.value = RegisterUIState.Loading

            val request = RegisterRequest(
                email = current.email,
                password = current.password,
                firstname = current.firstName,
                lastname = current.lastName,
                dniNumber = current.dniNumber,
                birthdate = current.birthdate,
                phone = current.phone,
                plateNumber = current.plateNumber,
                licenceNumber = current.licenceNumber,
                vehicleColor = current.vehicleColor,
                rolUser = current.rolUser
            )

            // ✅ Usar el RegisterUseCase en lugar de delay()
            registerUseCase(request)
                .onSuccess { message ->
                    _uiState.value = RegisterUIState.Success(message)
                }
                .onFailure { error ->
                    _uiState.value = RegisterUIState.Error(error.message.orEmpty())
                }
        }
    }

    fun reset() {
        _uiState.value = RegisterUIState.Idle
    }
}