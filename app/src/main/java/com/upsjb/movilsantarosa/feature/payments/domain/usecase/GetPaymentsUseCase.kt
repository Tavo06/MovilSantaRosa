package com.upsjb.movilsantarosa.feature.payments.domain.usecase

import com.upsjb.movilsantarosa.feature.auth.domain.model.UserRole
import com.upsjb.movilsantarosa.feature.auth.domain.usecase.CurrentUserUseCase
import com.upsjb.movilsantarosa.feature.payments.domain.model.Payment
import com.upsjb.movilsantarosa.feature.payments.domain.repository.PaymentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetPaymentsUseCase @Inject constructor(
    private val repository: PaymentRepository,
    private val currentUserUseCase: CurrentUserUseCase
) {
    operator fun invoke(): Flow<List<Payment>> {

        return flow {

            val user = currentUserUseCase()
                .getOrElse {
                    throw Exception("No existe una sesión activa.")
                }

            val finesFlow = when (user.role) {

                UserRole.ADMIN ->
                    repository.getAllPayments()

                UserRole.PARTNER ->
                    repository.getPaymentByEmail(user.email)
            }

            emitAll(finesFlow)
        }
    }
}