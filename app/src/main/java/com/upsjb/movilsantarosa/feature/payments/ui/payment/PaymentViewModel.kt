package com.upsjb.movilsantarosa.feature.payments.ui.payment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upsjb.movilsantarosa.feature.fine.domain.usecase.GetFinesUseCase
import com.upsjb.movilsantarosa.feature.fine.ui.fine.FineUiState
import com.upsjb.movilsantarosa.feature.payments.domain.usecase.GetPaymentsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val getPaymentsUseCase: GetPaymentsUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<PaymentUiState>(PaymentUiState.Loading)
    val uiState: StateFlow<PaymentUiState> = _uiState.asStateFlow()

    init {
        loadPayments()
    }

    fun loadPayments() {
        if (_uiState.value is PaymentUiState.Success) return

        viewModelScope.launch {
            _uiState.value = PaymentUiState.Loading
            getPaymentsUseCase()
                .onSuccess { payments ->

                    _uiState.value = PaymentUiState.Success(
                        payments = payments,
                    )
                }
                .onFailure { error ->

                    _uiState.value = PaymentUiState.Error(
                        error.message ?: "Error al cargar pagos"
                    )
                }
        }
    }

    fun updateQuery(query: String) {

        _uiState.update { state ->

            when (state) {

                is PaymentUiState.Success -> {
                    val filtered = if (query.isBlank()) {
                        state.payments
                    } else {
                        state.payments.filter { payment ->
                            payment.memberName.contains(query, true) ||
                                    payment.memberDniNumber.contains(query, true)
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
