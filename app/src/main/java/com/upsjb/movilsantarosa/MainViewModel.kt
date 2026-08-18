package com.upsjb.movilsantarosa

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upsjb.movilsantarosa.feature.auth.domain.model.UserRole
import com.upsjb.movilsantarosa.feature.auth.domain.model.UserStatus
import com.upsjb.movilsantarosa.feature.auth.domain.usecase.CurrentUserUseCase
import com.upsjb.movilsantarosa.feature.auth.domain.usecase.EnsureActiveSessionUseCase
import com.upsjb.movilsantarosa.feature.auth.domain.usecase.GetLocalSessionIdUseCase
import com.upsjb.movilsantarosa.feature.auth.domain.usecase.LogoutUseCase
import com.upsjb.movilsantarosa.feature.auth.domain.usecase.ObserveActiveSessionUseCase
import com.upsjb.movilsantarosa.feature.auth.domain.usecase.ObserveUserStatusUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val currentUserUseCase: CurrentUserUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val observeActiveSessionUseCase: ObserveActiveSessionUseCase,
    private val getLocalSessionIdUseCase: GetLocalSessionIdUseCase,
    private val ensureActiveSessionUseCase: EnsureActiveSessionUseCase,
    private val observeUserStatusUseCase: ObserveUserStatusUseCase,
) : ViewModel() {

    private val _session =
        MutableStateFlow<SessionState>(SessionState.Loading)
    val session: StateFlow<SessionState> = _session.asStateFlow()

    private var sessionWatchJob: Job? = null

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
                    if (user.status == UserStatus.ACTIVE) {
                        _session.value = SessionState.LoggedIn(user.role)
                        startSessionWatcher(user.uid)
                    } else {
                        sessionWatchJob?.cancel()
                        _session.value = SessionState.PendingApproval(user.status)
                    }
                }
                .onFailure {
                    sessionWatchJob?.cancel()
                    _session.value = SessionState.LoggedOut
                }
        }
    }

    private fun startSessionWatcher(uid: String) {
        sessionWatchJob?.cancel()
        sessionWatchJob = viewModelScope.launch {

            ensureActiveSessionUseCase(uid)

            val localSessionId = getLocalSessionIdUseCase()

            combine(
                observeActiveSessionUseCase(uid),
                observeUserStatusUseCase(uid)
            ) { remoteSessionId, remoteStatus ->
                remoteSessionId to remoteStatus
            }
                .catch { /* fail open: a transient listener error must not force a logout */ }
                .collect { (remoteSessionId, remoteStatus) ->
                    when {
                        localSessionId != null &&
                            remoteSessionId != null &&
                            remoteSessionId != localSessionId -> {
                            logout()
                        }

                        remoteStatus != null && remoteStatus != UserStatus.ACTIVE -> {
                            sessionWatchJob?.cancel()
                            _session.value = SessionState.PendingApproval(remoteStatus)
                        }
                    }
                }
        }
    }

    fun logout() {
        sessionWatchJob?.cancel()

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