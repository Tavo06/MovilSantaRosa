package com.upsjb.movilsantarosa

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upsjb.movilsantarosa.domain.authentic.usecase.CurrentUserUseCase
import com.upsjb.movilsantarosa.domain.authentic.usecase.LogoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val currentUserUseCase: CurrentUserUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    private val _session =
        MutableStateFlow<SessionState>(SessionState.Loading)
    val session: StateFlow<SessionState> = _session.asStateFlow()

    init {
        refreshSession()
    }

    fun refreshSession() {
        viewModelScope.launch {
            if (_session.value == SessionState.Loading) {
                delay(1000)
            }

            _session.value =
                if (currentUserUseCase() != null) {
                    SessionState.LoggedIn
                } else {
                    SessionState.LoggedOut
                }
        }
    }

    fun logout() {
        viewModelScope.launch {

            logoutUseCase()
                .onSuccess {
                    _session.value = SessionState.LoggedOut
                }
                .onFailure {
                    // puedes manejar el error si quieres
                }
        }
    }
}

sealed interface SessionState {
    data object Loading : SessionState
    data object LoggedOut : SessionState
    data object LoggedIn : SessionState
}