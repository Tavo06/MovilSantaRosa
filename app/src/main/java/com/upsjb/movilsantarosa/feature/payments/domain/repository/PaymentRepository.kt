package com.upsjb.movilsantarosa.feature.payments.domain.repository

import com.upsjb.movilsantarosa.feature.payments.domain.model.Payment
import kotlinx.coroutines.flow.Flow

interface PaymentRepository {

    fun getPaymentByEmail(email: String): Flow<List<Payment>>

    fun getAllPayments(): Flow<List<Payment>>

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