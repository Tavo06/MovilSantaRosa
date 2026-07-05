package com.upsjb.movilsantarosa.feature.fine.ui.fine

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upsjb.movilsantarosa.feature.fine.domain.usecase.GetFinesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.collections.filter

@HiltViewModel
class FineViewModel @Inject constructor(
    private val getFinesUseCase: GetFinesUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<FineUiState>(FineUiState.Loading)
    val uiState: StateFlow<FineUiState> = _uiState.asStateFlow()

    init {
        loadFines()
    }

    fun loadFines() {
        if (_uiState.value is FineUiState.Success) return

        viewModelScope.launch {
            _uiState.value = FineUiState.Loading
            getFinesUseCase()
                .onSuccess { fines ->

                    _uiState.value = FineUiState.Success(
                        fines = fines,
                    )
                }
                .onFailure { error ->

                    _uiState.value = FineUiState.Error(
                        error.message ?: "Error al cargar multas"
                    )
                }
        }
    }

    fun updateQuery(query: String) {

        _uiState.update { state ->

            when (state) {

                is FineUiState.Success -> {
                    val filtered = if (query.isBlank()) {
                        state.fines
                    } else {
                        state.fines.filter { fine ->
                            fine.memberName.contains(query, true) ||
                                    fine.memberEmail.contains(query, true) ||
                                    fine.reason.name.contains(query, true) ||
                                    fine.customReason.contains(query, true)
                        }
                    }

                    state.copy(
                        query = query,
                    )
                }

                else -> state
            }
        }
    }
}