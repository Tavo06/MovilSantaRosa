package com.upsjb.movilsantarosa.feature.auth.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upsjb.movilsantarosa.feature.auth.domain.usecase.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<LoginUIState>(LoginUIState.Idle)
    val uiState: StateFlow<LoginUIState> = _uiState.asStateFlow()

    fun login(email: String, password: String) {

        if (email.isBlank() || password.isBlank()) {
            _uiState.value =
                LoginUIState.Error("Debe ingresar el correo y la contraseña.")
            return
        }

        _uiState.value = LoginUIState.Loading

        viewModelScope.launch {

            loginUseCase(email, password)
                .onSuccess {
                    _uiState.value = LoginUIState.Success(it)
                }
                .onFailure {

                    _uiState.value = LoginUIState.Error(
                        "Correo o contraseña incorrectos."
                    )

                }
        }
    }

    fun reset() {
        _uiState.value = LoginUIState.Idle
    }
}
