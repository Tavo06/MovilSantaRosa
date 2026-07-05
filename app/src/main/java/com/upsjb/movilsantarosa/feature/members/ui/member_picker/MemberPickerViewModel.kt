package com.upsjb.movilsantarosa.feature.members.ui.member_picker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upsjb.movilsantarosa.feature.members.domain.model.Member
import com.upsjb.movilsantarosa.feature.members.domain.usecase.GetAllMembersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MemberPickerViewModel @Inject constructor(
    private val getAllMembersUseCase: GetAllMembersUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MemberPickerUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadMembers()
    }

    fun loadMembers() {
        viewModelScope.launch {

            _uiState.update {
                it.copy(loading = true)
            }

            getAllMembersUseCase()
                .onSuccess { members ->
                    _uiState.update {
                        it.copy(
                            loading = false,
                            members = members
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            loading = false,
                            error = error.message ?: "Error al cargar miembros"
                        )
                    }
                }
        }
    }

    fun updateQuery(query: String) {
        _uiState.update {
            it.copy(query = query)
        }
    }
}

data class MemberPickerUiState(
    val members: List<Member> = emptyList(),
    val query: String = "",
    val loading: Boolean = false,
    val error: String? = null
) {

    val filteredMembers: List<Member>
        get() = if (query.isBlank()) {
            members
        } else {
            members.filter {
                it.fullName.contains(query, ignoreCase = true) ||
                        it.email.contains(query, ignoreCase = true) ||
                        it.dniNumber.contains(query)
            }
        }
}