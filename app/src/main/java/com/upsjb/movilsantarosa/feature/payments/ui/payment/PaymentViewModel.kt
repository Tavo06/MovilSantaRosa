package com.upsjb.movilsantarosa.feature.payments.ui.payment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upsjb.movilsantarosa.feature.payments.domain.model.Payment
import com.upsjb.movilsantarosa.feature.payments.domain.usecase.GetPaymentsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val getPaymentsUseCase: GetPaymentsUseCase
) : ViewModel() {

    private val query = MutableStateFlow("")
    private val paymentsFlow = MutableStateFlow<List<Payment>>(emptyList())

    init {
        observePayments()
    }

    private fun observePayments() {
        viewModelScope.launch {
            getPaymentsUseCase()
                .collect { payments ->
                    paymentsFlow.value = payments
                }
        }
    }

    val uiState = combine(
        paymentsFlow,
        query
    ) { payments, query ->

        val filtered = if (query.isBlank()) {
            payments
        } else {
            payments.filter { payment ->
                payment.memberName.contains(query, true) ||
                        payment.memberDniNumber.contains(query, true)
            }
        }

        PaymentUiState.Success(
            payments = filtered,
            query = query
        )
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = PaymentUiState.Loading
        )

    fun updateQuery(query: String) {
        this.query.value = query
    }
}