package com.upsjb.movilsantarosa.feature.members.ui.pending_registrations

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upsjb.movilsantarosa.feature.auth.domain.model.UserStatus
import com.upsjb.movilsantarosa.feature.members.domain.model.Member
import com.upsjb.movilsantarosa.feature.members.domain.usecase.GetPendingMembersUseCase
import com.upsjb.movilsantarosa.feature.members.domain.usecase.UpdateMemberStatusUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PendingRegistrationsViewModel @Inject constructor(
    private val getPendingMembersUseCase: GetPendingMembersUseCase,
    private val updateMemberStatusUseCase: UpdateMemberStatusUseCase
) : ViewModel() {

    val uiState: StateFlow<PendingRegistrationsUiState> = getPendingMembersUseCase()
        .map<List<Member>, PendingRegistrationsUiState> { members ->
            PendingRegistrationsUiState.Success(members)
        }
        .catch { error ->
            emit(
                PendingRegistrationsUiState.Error(
                    error.message ?: "No se pudieron cargar las solicitudes de registro."
                )
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = PendingRegistrationsUiState.Loading
        )

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    fun approve(member: Member) {
        updateStatus(member, UserStatus.ACTIVE)
    }

    fun reject(member: Member) {
        updateStatus(member, UserStatus.REJECTED)
    }

    fun clearError() {
        _errorMessage.value = null
    }

    private fun updateStatus(member: Member, status: UserStatus) {
        viewModelScope.launch {
            updateMemberStatusUseCase(member.uid, status)
                .onFailure { error ->
                    _errorMessage.value = error.message
                        ?: "No se pudo actualizar la solicitud de registro."
                }
        }
    }
}
