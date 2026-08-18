package com.upsjb.movilsantarosa

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upsjb.movilsantarosa.feature.auth.domain.model.UserRole
import com.upsjb.movilsantarosa.feature.auth.domain.model.UserStatus
import com.upsjb.movilsantarosa.feature.auth.domain.usecase.CurrentUserUseCase
import com.upsjb.movilsantarosa.feature.auth.domain.usecase.LogoutUseCase
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
    private val logoutUseCase: LogoutUseCase,
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

            currentUserUseCase()
                .onSuccess { user ->
                    _session.value = if (user.status == UserStatus.ACTIVE) {
                        SessionState.LoggedIn(user.role)
                    } else {
                        SessionState.PendingApproval(user.status)
                    }
                }
                .onFailure {
                    _session.value = SessionState.LoggedOut
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

    data class LoggedIn(
        val role: UserRole
    ) : SessionState

    data class PendingApproval(
        val status: UserStatus
    ) : SessionState
}