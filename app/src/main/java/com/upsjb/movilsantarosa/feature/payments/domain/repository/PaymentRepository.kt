package com.upsjb.movilsantarosa.feature.payments.domain.repository

import com.upsjb.movilsantarosa.feature.payments.domain.model.Payment

interface PaymentRepository {

    suspend fun getPaymentByEmail(
        email: String
    ): Result<List<Payment>>

    suspend fun getAllPayments(): Result<List<Payment>>

    suspend fun getPaymentById(
        id: String
    ): Result<Payment>

    suspend fun registerPayment(
        payment: Payment
    ): Result<Unit>

    suspend fun updatePayment(
        payment: Payment
    ): Result<Unit>
}