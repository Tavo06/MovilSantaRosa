package com.upsjb.movilsantarosa.ui.feature.register

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upsjb.movilsantarosa.domain.authentic.request.RegisterRequest
import com.upsjb.movilsantarosa.domain.authentic.request.RolUser
import com.upsjb.movilsantarosa.domain.authentic.usecase.RegisterUseCase
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
        _uiState.update {
            it.copy(
                form = it.form.transform()
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
                rolUser = RolUser.PARTNER
            )

            registerUseCase(request)
                .onSuccess { user ->
                    _uiState.update {
                        it.copy(
                            uiState = RegisterActionUiState.Success(user)
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            uiState = RegisterActionUiState.Error(
                                error.message.orEmpty()
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

    private fun validateForm(form: RegisterFormState): String? {

        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

        return when {
            form.firstName.isBlank() ->
                "Ingrese sus nombres."

            form.lastName.isBlank() ->
                "Ingrese sus apellidos."

            form.dniNumber.length != 8 ->
                "El DNI debe tener 8 dígitos."

            form.birthDate.isBlank() ->
                "Seleccione su fecha de nacimiento."

            else -> {
                val birthDate = try {
                    LocalDate.parse(form.birthDate, formatter)
                } catch (_: Exception) {
                    return "La fecha de nacimiento no es válida."
                }

                if (Period.between(birthDate, LocalDate.now()).years < 18) {
                    "Debe ser mayor de 18 años."
                } else if (form.email.isBlank()) {
                    "Ingrese su correo electrónico."
                } else if (!Patterns.EMAIL_ADDRESS.matcher(form.email).matches()) {
                    "El correo electrónico no es válido."
                } else if (form.phone.length < 9) {
                    "Ingrese un número de teléfono válido."
                } else if (form.plateNumber.length < 6) {
                    "Ingrese una placa válida."
                } else if (form.vehicleColor.isBlank()) {
                    "Ingrese el color del vehículo."
                } else if (form.licenceNumber.isBlank()) {
                    "Ingrese el número de licencia."
                } else if (form.password.length < 6) {
                    "La contraseña debe tener al menos 6 caracteres."
                } else if (form.password != form.confirmPassword) {
                    "Las contraseñas no coinciden."
                } else {
                    null
                }
            }
        }
    }
}