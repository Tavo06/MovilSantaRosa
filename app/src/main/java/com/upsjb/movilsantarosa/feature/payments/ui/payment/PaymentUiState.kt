package com.upsjb.movilsantarosa.feature.payments.ui.payment

import com.upsjb.movilsantarosa.feature.payments.domain.model.Payment

sealed class PaymentUiState {

    data object Loading : PaymentUiState()

    data class Success(
        val query: String = "",
        val payments: List<Payment> = emptyList(),
    ) : PaymentUiState()

    data class Error(
        val message: String
    ) : PaymentUiState()
}