package com.upsjb.movilsantarosa.feature.auth.ui.register

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upsjb.movilsantarosa.feature.auth.domain.model.UserRole
import com.upsjb.movilsantarosa.feature.auth.domain.model.UserStatus
import com.upsjb.movilsantarosa.feature.auth.domain.request.RegisterRequest
import com.upsjb.movilsantarosa.feature.auth.domain.usecase.RegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    fun updateForm(
        transform: RegisterFormState.() -> RegisterFormState
    ) {
        _uiState.update { state ->

            val form = state.form.transform()

            state.copy(
                form = form.copy(
                    firstName = form.firstName.onlyLetters(),
                    lastName = form.lastName.onlyLetters(),
                    phone = form.phone.onlyNumbers().take(9),
                    dniNumber = form.dniNumber.onlyNumbers().take(8),
                    plateNumber = form.plateNumber.uppercase().take(6)
                )
            )
        }
    }

    fun register() {
        val form = _uiState.value.form

        validateForm(form)?.let { message ->
            _uiState.update {
                it.copy(
                    uiState = RegisterActionUiState.Error(message)
                )
            }
            return
        }

        viewModelScope.launch {
            _uiState.update {
                it.copy(uiState = RegisterActionUiState.Loading)
            }

            val request = RegisterRequest(
                email = form.email,
                password = form.password,
                firstname = form.firstName,
                lastname = form.lastName,
                dniNumber = form.dniNumber,
                birthdate = form.birthDate,
                phone = form.phone,
                plateNumber = form.plateNumber,
                licenceNumber = form.licenceNumber,
                vehicleColor = form.vehicleColor,
                role = UserRole.PARTNER,
                status = UserStatus.INACTIVE,
            )

            registerUseCase(request)
                .onSuccess {
                    _uiState.update {
                        it.copy(uiState = RegisterActionUiState.Success)
                    }
                }
                .onFailure {
                    _uiState.update {
                        it.copy(
                            uiState = RegisterActionUiState.Error(
                                "No se pudo registrar el usuario. Verifique los datos ingresados."
                            )
                        )
                    }
                }
        }
    }

    fun reset() {
        _uiState.update {
            it.copy(uiState = RegisterActionUiState.Idle)
        }
    }

    private fun String.onlyLetters(): String {
        return filter {
            it.isLetter() || it == ' '
        }
    }

    private fun String.onlyNumbers(): String {
        return filter {
            it.isDigit()
        }
    }

    private fun isValidPlate(plate: String): Boolean {
        return Regex("^[A-Z]{3}[0-9]{3}$").matches(plate)
    }

    private fun validateForm(form: RegisterFormState): String? {

        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

        return when {

            form.firstName.isBlank() ->
                "Ingrese sus nombres."

            form.lastName.isBlank() ->
                "Ingrese sus apellidos."

            form.dniNumber.length != 8 ->
                "El DNI debe tener exactamente 8 dígitos."

            form.birthDate.isBlank() ->
                "Seleccione su fecha de nacimiento."

            else -> {
                val birthDate = try {
                    LocalDate.parse(form.birthDate, formatter)
                } catch (_: Exception) {
                    return "La fecha de nacimiento no es válida. Use el formato dd/MM/yyyy."
                }

                if (Period.between(birthDate, LocalDate.now()).years < 18) {
                    "Debe ser mayor de 18 años."
                } else if (form.email.isBlank()) {
                    "Ingrese su correo electrónico."
                } else if (!Patterns.EMAIL_ADDRESS.matcher(form.email).matches()) {
                    "El correo electrónico no es válido."
                } else if (form.phone.length != 9) {
                    "El número celular debe tener exactamente 9 dígitos."
                } else if (form.plateNumber.isBlank()) {
                    "Ingrese la placa del vehículo."
                } else if (!isValidPlate(form.plateNumber)) {
                    "La placa debe tener 3 letras y 3 números. Ejemplo: ABC123."
                } else if (form.vehicleColor.isBlank()) {
                    "Ingrese el color del vehículo."
                } else if (form.licenceNumber.isBlank()) {
                    "Ingrese el número de licencia."
                } else if (form.password.length < 6) {
                    "La contraseña debe tener al menos 6 caracteres."
                } else {
                    null
                }
            }
        }
    }
}