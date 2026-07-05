package com.upsjb.movilsantarosa.feature.members.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upsjb.movilsantarosa.feature.members.domain.usecase.GetAllMembersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MemberViewModel @Inject constructor(
    private val getAllMembersUseCase: GetAllMembersUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<MemberUIState>(MemberUIState.Loading)
    val uiState: StateFlow<MemberUIState> = _uiState.asStateFlow()

    init {
        getAllMembers()
    }
    fun getAllMembers() {

        if (_uiState.value is MemberUIState.Success) return

        viewModelScope.launch {

            _uiState.value = MemberUIState.Loading

            getAllMembersUseCase()
                .onSuccess {
                    _uiState.value = MemberUIState.Success(members = it)
                }
                .onFailure {
                    _uiState.value = MemberUIState.Error(
                        it.message ?: "Ocurrió un error"
                    )
                }
        }
    }

    fun updateQuery(query: String) {
        _uiState.update { state ->

            when (state) {
                is MemberUIState.Success ->
                    state.copy(query = query)

                else -> state
            }
        }
    }

}