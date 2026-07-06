package com.upsjb.movilsantarosa.feature.fine.ui.fine_picker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upsjb.movilsantarosa.feature.fine.domain.usecase.GetFinesByEmailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FinePickerViewModel @Inject constructor(
    private val getFinesByEmailUseCase: GetFinesByEmailUseCase
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<FinePickerUiState>(FinePickerUiState.Loading)

    val uiState = _uiState.asStateFlow()

    fun loadFines(memberEmail: String) {
        viewModelScope.launch {

            _uiState.value = FinePickerUiState.Loading

            getFinesByEmailUseCase(memberEmail)
                .onSuccess { fines ->
                    _uiState.value = FinePickerUiState.Success(
                        fines = fines
                    )
                }
                .onFailure { error ->
                    _uiState.value = FinePickerUiState.Error(
                        error.message ?: "No se pudieron cargar las multas."
                    )
                }
        }
    }

    fun updateQuery(query: String) {
        val state = _uiState.value

        if (state is FinePickerUiState.Success) {
            _uiState.value = state.copy(query = query)
        }
    }
}