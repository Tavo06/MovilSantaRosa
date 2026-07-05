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

    private val _uiState =
        MutableStateFlow<MemberPickerUiState>(MemberPickerUiState.Loading)

    val uiState = _uiState.asStateFlow()

    init {
        loadMembers()
    }

    fun loadMembers() {
        viewModelScope.launch {

            _uiState.value = MemberPickerUiState.Loading

            getAllMembersUseCase()
                .onSuccess { members ->
                    _uiState.value =
                        MemberPickerUiState.Success(
                            members = members
                        )
                }
                .onFailure { error ->
                    _uiState.value =
                        MemberPickerUiState.Error(
                            error.message ?: "No se pudieron cargar los socios."
                        )
                }
        }
    }

    fun updateQuery(query: String) {
        val state = _uiState.value

        if (state is MemberPickerUiState.Success) {
            _uiState.value = state.copy(query = query)
        }
    }
}