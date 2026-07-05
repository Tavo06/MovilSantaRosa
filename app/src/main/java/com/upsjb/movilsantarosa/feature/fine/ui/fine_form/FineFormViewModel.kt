package com.upsjb.movilsantarosa.feature.fine.ui.fine_form

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upsjb.movilsantarosa.feature.fine.data.model.FineReason
import com.upsjb.movilsantarosa.feature.fine.data.model.FineStatus
import com.upsjb.movilsantarosa.feature.fine.domain.model.Fine
import com.upsjb.movilsantarosa.feature.fine.domain.model.toDomain
import com.upsjb.movilsantarosa.feature.fine.domain.model.toForm
import com.upsjb.movilsantarosa.feature.fine.domain.usecase.GetFineByIdUseCase
import com.upsjb.movilsantarosa.feature.fine.domain.usecase.RegisterFineUseCase
import com.upsjb.movilsantarosa.feature.fine.domain.usecase.UpdateFineUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FineFormViewModel @Inject constructor(
    private val registerFineUseCase: RegisterFineUseCase,
    private val updateFineUseCase: UpdateFineUseCase,
    private val getFineByIdUseCase: GetFineByIdUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(FineFormUiState())
    val uiState = _uiState.asStateFlow()

    fun updateForm(transform: FineFormState.() -> FineFormState) {
        _uiState.update {
            it.copy(form = it.form.transform())
        }
    }

    fun setMode(mode: FineFormMode) {
        _uiState.update {
            it.copy(mode = mode)
        }
    }

    fun loadFine(id: String) {
        viewModelScope.launch {

            _uiState.update {
                it.copy(actionState = FineFormActionState.Loading)
            }

            getFineByIdUseCase(id)
                .onSuccess { fine ->
                    _uiState.update {
                        it.copy(
                            form = fine.toForm(),
                            actionState = FineFormActionState.Idle
                        )
                    }
                }
                .onFailure {
                    _uiState.update {
                        it.copy(
                            actionState = FineFormActionState.Error("No se pudo cargar la multa")
                        )
                    }
                }
        }
    }

    fun saveFine() {
        viewModelScope.launch {

            val state = _uiState.value
            val fine = state.form.toDomain()

            _uiState.update {
                it.copy(actionState = FineFormActionState.Loading)
            }

            val result = when (state.mode) {

                FineFormMode.CREATE -> registerFineUseCase(fine)

                FineFormMode.EDIT -> updateFineUseCase(fine)

                FineFormMode.READ_ONLY -> {
                    return@launch
                }
            }

            result
                .onSuccess {
                    _uiState.update {
                        it.copy(actionState = FineFormActionState.Success)
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            actionState = FineFormActionState.Error(
                                error.message ?: "Error desconocido"
                            )
                        )
                    }
                }
        }
    }

    fun resetAction() {
        _uiState.update {
            it.copy(actionState = FineFormActionState.Idle)
        }
    }
}