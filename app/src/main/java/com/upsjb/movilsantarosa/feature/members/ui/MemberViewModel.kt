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

    private val _uiState = MutableStateFlow< MemberUiState>(MemberUiState.Loading)
    val uiState: StateFlow<MemberUiState> = _uiState.asStateFlow()

    init {
        getAllMembers()
    }
    fun getAllMembers() {

        if (_uiState.value is MemberUiState.Success) return

        viewModelScope.launch {

            _uiState.value = MemberUiState.Loading

            getAllMembersUseCase()
                .onSuccess {
                    _uiState.value = MemberUiState.Success(members = it)
                }
                .onFailure {
                    _uiState.value = MemberUiState.Error(
                        it.message ?: "Ocurrió un error"
                    )
                }
        }
    }

    fun updateQuery(query: String) {
        _uiState.update { state ->

            when (state) {
                is MemberUiState.Success ->
                    state.copy(query = query)

                else -> state
            }
        }
    }

}