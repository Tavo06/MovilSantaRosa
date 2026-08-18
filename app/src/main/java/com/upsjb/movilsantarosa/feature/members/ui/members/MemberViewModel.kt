package com.upsjb.movilsantarosa.feature.members.ui.members

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upsjb.movilsantarosa.feature.auth.domain.model.UserRole
import com.upsjb.movilsantarosa.feature.auth.domain.model.UserStatus
import com.upsjb.movilsantarosa.feature.auth.domain.usecase.CurrentUserUseCase
import com.upsjb.movilsantarosa.feature.members.domain.model.Member
import com.upsjb.movilsantarosa.feature.members.domain.usecase.GetActiveMembersUseCase
import com.upsjb.movilsantarosa.feature.members.domain.usecase.GetAllMembersUseCase
import com.upsjb.movilsantarosa.feature.members.domain.usecase.UpdateMemberProfileUseCase
import com.upsjb.movilsantarosa.feature.members.domain.usecase.UpdateMemberStatusUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MemberViewModel @Inject constructor(
    private val getAllMembersUseCase: GetAllMembersUseCase,
    private val getActiveMembersUseCase: GetActiveMembersUseCase,
    private val currentUserUseCase: CurrentUserUseCase,
    private val updateMemberProfileUseCase: UpdateMemberProfileUseCase,
    private val updateMemberStatusUseCase: UpdateMemberStatusUseCase
) : ViewModel() {

    private val query = MutableStateFlow("")

    val uiState = flow {

        val isAdmin = currentUserUseCase()
            .getOrNull()
            ?.role == UserRole.ADMIN

        val membersFlow = if (isAdmin) {
            getAllMembersUseCase()
        } else {
            getActiveMembersUseCase()
        }

        emitAll(
            combine(membersFlow, query) { members, query ->
                MemberUiState.Success(
                    members = members,
                    query = query,
                    isAdmin = isAdmin
                )
            }
        )
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = MemberUiState.Loading
        )

    private val _actionState = MutableStateFlow<MemberActionState>(MemberActionState.Idle)
    val actionState = _actionState.asStateFlow()

    fun updateQuery(query: String) {
        this.query.value = query
    }

    fun updateMemberProfile(member: Member) {
        viewModelScope.launch {

            _actionState.value = MemberActionState.Loading

            updateMemberProfileUseCase(member)
                .onSuccess {
                    _actionState.value = MemberActionState.Success
                }
                .onFailure { error ->
                    _actionState.value = MemberActionState.Error(
                        error.message ?: "No se pudo actualizar el socio."
                    )
                }
        }
    }

    fun toggleMemberStatus(member: Member) {
        val newStatus = if (member.status == UserStatus.ACTIVE) {
            UserStatus.INACTIVE
        } else {
            UserStatus.ACTIVE
        }

        viewModelScope.launch {

            _actionState.value = MemberActionState.Loading

            updateMemberStatusUseCase(member.uid, newStatus)
                .onSuccess {
                    _actionState.value = MemberActionState.Success
                }
                .onFailure { error ->
                    _actionState.value = MemberActionState.Error(
                        error.message ?: "No se pudo actualizar el estado del socio."
                    )
                }
        }
    }

    fun resetActionState() {
        _actionState.value = MemberActionState.Idle
    }
}
