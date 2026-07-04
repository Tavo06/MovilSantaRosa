package com.upsjb.movilsantarosa.ui.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upsjb.movilsantarosa.domain.authentic.usecase.CurrentUserUseCase
import com.upsjb.movilsantarosa.domain.home.usecase.GetUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val currentUserUseCase: CurrentUserUseCase,
    private val getUserUseCase: GetUserUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUIState>(HomeUIState.Loading)
    val uiState: StateFlow<HomeUIState> = _uiState.asStateFlow()

    init {
        validateSession()
    }

    private fun validateSession() {
        viewModelScope.launch {
            val user = currentUserUseCase()
            if (user != null) {
                getUserUseCase(user.uid).onSuccess { data ->
                    _uiState.value = HomeUIState.Success(data)
                }.onFailure {
                    _uiState.value = HomeUIState.Error("Usuario no encontrado")
                }
            } else {
                _uiState.value = HomeUIState.Error("Usuario no logueado")
            }

        }
    }
}