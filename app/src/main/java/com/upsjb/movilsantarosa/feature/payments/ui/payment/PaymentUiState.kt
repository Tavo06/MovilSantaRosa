package com.upsjb.movilsantarosa.feature.payments.ui.payment

import com.upsjb.movilsantarosa.feature.payments.domain.model.Payment

sealed class PaymentUiState {

    data object Loading : PaymentUiState()

    data class Success(
        val query: String = "",
        val payments: List<Payment> = emptyList(),
    ) : PaymentUiState() {
        val filteredPayments: List<Payment>
            get() = if (query.isBlank()) {
                payments
            } else {
                payments.filter {
                    it.memberDniNumber.contains(query, true) ||
                            it.memberName.contains(query, true)
                }
            }
    }

    data class Error(
        val message: String
    ) : PaymentUiState()
}