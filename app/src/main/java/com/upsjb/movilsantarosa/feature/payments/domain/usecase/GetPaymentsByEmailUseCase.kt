package com.upsjb.movilsantarosa.feature.payments.domain.usecase

import com.upsjb.movilsantarosa.feature.payments.domain.model.Payment
import com.upsjb.movilsantarosa.feature.payments.domain.repository.PaymentRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPaymentsByEmailUseCase @Inject constructor(
    private val repository: PaymentRepository
) {

    operator fun invoke(email: String): Flow<List<Payment>> {
        return repository.getPaymentByEmail(email)
    }
}
