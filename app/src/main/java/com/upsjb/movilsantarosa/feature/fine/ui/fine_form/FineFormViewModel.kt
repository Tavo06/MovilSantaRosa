package com.upsjb.movilsantarosa.feature.fine.ui.fine_form

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upsjb.movilsantarosa.feature.auth.domain.model.UserRole
import com.upsjb.movilsantarosa.feature.auth.domain.usecase.CurrentUserUseCase
import com.upsjb.movilsantarosa.feature.fine.data.model.FineReason
import com.upsjb.movilsantarosa.feature.fine.domain.model.toDomain
import com.upsjb.movilsantarosa.feature.fine.domain.model.toForm
import com.upsjb.movilsantarosa.feature.fine.domain.usecase.GetFineByIdUseCase
import com.upsjb.movilsantarosa.feature.fine.domain.usecase.RegisterFineUseCase
import com.upsjb.movilsantarosa.feature.fine.domain.usecase.UpdateFineUseCase
import com.upsjb.movilsantarosa.feature.members.domain.model.Member
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FineFormViewModel @Inject constructor(
    private val registerFineUseCase: RegisterFineUseCase,
    private val updateFineUseCase: UpdateFineUseCase,
    private val getFineByIdUseCase: GetFineByIdUseCase,
    private val currentUserUseCase: CurrentUserUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(FineFormUiState())
    val uiState = _uiState.asStateFlow()
    init {
        loadCurrentUser()
    }
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
    private fun loadCurrentUser() {
        viewModelScope.launch {
            currentUserUseCase()
                .onSuccess { user ->
                    _uiState.update {
                        it.copy(
                            isAdmin = user.role == UserRole.ADMIN
                        )
                    }
                }
        }
    }

    fun loadFine(id: String) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    mode = FineFormMode.READ_ONLY,
                    actionState = FineFormActionState.Loading
                )
            }

            getFineByIdUseCase(id)
                .onSuccess { fine ->
                    _uiState.update {
                        it.copy(
                            form = fine.toForm(),
                            fineId = fine.id,
                            actionState = FineFormActionState.Idle
                        )
                    }
                }
                .onFailure {
                    _uiState.update {
                        it.copy(
                            actionState = FineFormActionState.Error(
                                "No se pudo cargar la multa."
                            )
                        )
                    }
                }
        }
    }

    fun saveFine() {
        viewModelScope.launch {
            val state = _uiState.value
            val form = state.form

            validateForm(form)?.let { message ->
                _uiState.update {
                    it.copy(actionState = FineFormActionState.Error(message))
                }
                return@launch
            }

            val fine = form.toDomain()

            _uiState.update {
                it.copy(actionState = FineFormActionState.Loading)
            }

            val result = when (state.mode) {
                FineFormMode.CREATE -> registerFineUseCase(fine)
                FineFormMode.EDIT -> updateFineUseCase(fine)
                FineFormMode.READ_ONLY -> return@launch
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
                                error.message ?: "No se pudo guardar la multa."
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

    fun selectMember(member: Member) {
        _uiState.update {
            it.copy(
                form = it.form.copy(
                    memberName = member.fullName,
                    memberEmail = member.email,
                    memberDniNumber = member.dniNumber
                )
            )
        }
    }

    fun clearMember() {
        _uiState.update {
            it.copy(
                form = it.form.copy(
                    memberName = "",
                    memberEmail = "",
                    memberDniNumber = ""
                )
            )
        }
    }

    private fun validateForm(form: FineFormState): String? {
        return when {
            !form.isMemberFilled ->
                "Seleccione un socio antes de registrar la multa."

            form.reason == FineReason.OTHER && form.customReason.isBlank() ->
                "Ingrese el motivo personalizado de la multa."

            form.amount.isBlank() ->
                "Ingrese el monto de la multa."

            form.amount.toDoubleOrNull() == null ->
                "El monto debe ser un número válido."

            form.amount.toDouble() <= 0 ->
                "El monto debe ser mayor a 0."

            form.issuedAt.isBlank() ->
                "Seleccione la fecha de emisión."

            form.dueDate.isBlank() ->
                "Seleccione la fecha de vencimiento."

            else -> null
        }
    }
}