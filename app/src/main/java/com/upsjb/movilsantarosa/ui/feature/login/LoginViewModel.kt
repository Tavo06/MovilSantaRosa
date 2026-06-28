package com.upsjb.movilsantarosa.ui.feature.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upsjb.movilsantarosa.domain.authentic.usecase.LoginUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.onSuccess

class LoginViewModel (
    val loginUseCase: LoginUseCase
): ViewModel(){
    private val _uiState = MutableStateFlow<LoginUIState>(LoginUIState.Idle)
    val uiState: StateFlow<LoginUIState> = _uiState.asStateFlow()

    fun login(email: String, password: String){
        if (email.isBlank() || password.isBlank()) return

        _uiState.value = LoginUIState.Loading

        viewModelScope.launch {
            loginUseCase(email,password)
                .onSuccess {
                    _uiState.value = LoginUIState.Success(it)
                }
                .onFailure {
                    _uiState.value = LoginUIState.Error(it.message.orEmpty())
                }
        }
    }

    fun reset() {
        _uiState.value= LoginUIState.Idle
    }
}