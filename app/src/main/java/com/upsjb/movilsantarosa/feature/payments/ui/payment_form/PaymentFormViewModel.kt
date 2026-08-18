package com.upsjb.movilsantarosa.feature.payments.ui.payment_form

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upsjb.movilsantarosa.feature.fine.data.model.FineReason
import com.upsjb.movilsantarosa.feature.fine.data.model.FineStatus
import com.upsjb.movilsantarosa.feature.fine.domain.model.Fine
import com.upsjb.movilsantarosa.feature.members.domain.model.Member
import com.upsjb.movilsantarosa.feature.payments.domain.model.toDomain
import com.upsjb.movilsantarosa.feature.payments.domain.model.toForm
import com.upsjb.movilsantarosa.feature.payments.domain.usecase.GetPaymentByIdUseCase
import com.upsjb.movilsantarosa.feature.payments.domain.usecase.RegisterPaymentUseCase
import com.upsjb.movilsantarosa.feature.payments.domain.usecase.UpdatePaymentUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentFormViewModel @Inject constructor(
    private val registerPaymentUseCase: RegisterPaymentUseCase,
    private val updatePaymentUseCase: UpdatePaymentUseCase,
    private val getPaymentByIdUseCase: GetPaymentByIdUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(PaymentFormUiState())
    val uiState = _uiState.asStateFlow()

    fun updateForm(transform: PaymentFormState.() -> PaymentFormState) {
        _uiState.update {
            it.copy(form = it.form.transform())
        }
    }

    fun setMode(mode: PaymentFormMode) {
        _uiState.update {
            it.copy(mode = mode)
        }
    }

    fun loadPayment(id: String) {
        viewModelScope.launch {

            _uiState.update {
                it.copy(
                    mode = PaymentFormMode.READ_ONLY,
                    actionState = PaymentFormActionState.Loading
                )
            }

            getPaymentByIdUseCase(id)
                .onSuccess { payment ->
                    _uiState.update {
                        it.copy(
                            form = payment.toForm(),
                            paymentId = payment.id,
                            fineId = payment.fineId,
                            actionState = PaymentFormActionState.Idle
                        )
                    }
                }
                .onFailure {
                    _uiState.update {
                        it.copy(
                            actionState = PaymentFormActionState.Error(
                                "No se pudo cargar el pago"
                            )
                        )
                    }
                }
        }
    }

    fun savePayment() {
        viewModelScope.launch {

            val state = _uiState.value
            val form = state.form

            validateForm(form)?.let { message ->
                _uiState.update {
                    it.copy(
                        actionState = PaymentFormActionState.Error(message)
                    )
                }
                return@launch
            }

            val payment = form.toDomain()

            _uiState.update {
                it.copy(actionState = PaymentFormActionState.Loading)
            }

            val result = when (state.mode) {

                PaymentFormMode.CREATE -> registerPaymentUseCase(payment)

                PaymentFormMode.EDIT -> updatePaymentUseCase(payment)

                PaymentFormMode.READ_ONLY -> return@launch
            }

            result
                .onSuccess {
                    _uiState.update {
                        it.copy(actionState = PaymentFormActionState.Success)
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            actionState = PaymentFormActionState.Error(
                                error.message ?: "Error desconocido"
                            )
                        )
                    }
                }
        }
    }

    fun resetAction() {
        _uiState.update {
            it.copy(actionState = PaymentFormActionState.Idle)
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

    fun selectFine(fine: Fine) {
        _uiState.update {
            it.copy(
                form = it.form.copy(
                    fineId = fine.id,
                    fineAmount = fine.amount,
                    fineReason = fine.reason,
                    fineStatus = fine.status,
                    fineIssuedAt = fine.issuedAt,
                    fineCustomReason = fine.customReason,
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

    fun clearFine() {
        _uiState.update {
            it.copy(
                form = it.form.copy(
                    fineId = "",
                    fineReason = FineReason.OTHER,
                    fineStatus = FineStatus.PENDING,
                    fineIssuedAt = 0L,
                    fineCustomReason = "",
                )
            )
        }
    }

    private fun validateForm(form: PaymentFormState): String? {
        return when {

            !form.isMemberFilled ->
                "Seleccione un socio."

            !form.isFineFilled ->
                "Seleccione un multa."

            form.fineAmount <= 0 ->
                "El monto debe ser mayor a 0."

            else -> null
        }
    }
}