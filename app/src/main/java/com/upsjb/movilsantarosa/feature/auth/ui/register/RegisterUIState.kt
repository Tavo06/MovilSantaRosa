package com.upsjb.movilsantarosa.feature.auth.ui.register

import com.upsjb.movilsantarosa.feature.auth.domain.model.User

data class RegisterUiState(
    val form: RegisterFormState = RegisterFormState(),
    val uiState: RegisterActionUiState = RegisterActionUiState.Idle
)

data class RegisterFormState(
    val email: String = "",
    val password: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val dniNumber: String = "",
    val birthDate: String = "",
    val phone: String = "",
    val plateNumber: String = "",
    val licenceNumber: String = "",
    val vehicleColor: String = ""
)

sealed interface RegisterActionUiState {
    data object Idle : RegisterActionUiState
    data object Loading : RegisterActionUiState
    data object Success : RegisterActionUiState
    data class Error(val message: String) : RegisterActionUiState
}