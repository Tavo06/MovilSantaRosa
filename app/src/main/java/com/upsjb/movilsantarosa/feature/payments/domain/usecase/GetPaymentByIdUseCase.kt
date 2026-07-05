package com.upsjb.movilsantarosa.feature.payments.domain.usecase

import com.upsjb.movilsantarosa.feature.payments.domain.model.Payment
import com.upsjb.movilsantarosa.feature.payments.domain.repository.PaymentRepository
import javax.inject.Inject

class GetPaymentByIdUseCase @Inject constructor(
    private val repository: PaymentRepository
) {
    suspend operator fun invoke(id: String): Result<Payment> {
        return repository.getPaymentById(id)
    }
}