package com.upsjb.movilsantarosa.feature.payments.domain.usecase

import com.upsjb.movilsantarosa.feature.auth.domain.model.UserRole
import com.upsjb.movilsantarosa.feature.auth.domain.usecase.CurrentUserUseCase
import com.upsjb.movilsantarosa.feature.payments.domain.model.Payment
import com.upsjb.movilsantarosa.feature.payments.domain.repository.PaymentRepository
import javax.inject.Inject

class GetPaymentsUseCase @Inject constructor(
    private val repository: PaymentRepository,
    private val currentUserUseCase: CurrentUserUseCase
) {

    suspend operator fun invoke(): Result<List<Payment>> {

        val user = currentUserUseCase()
            .getOrElse {
                return Result.failure(
                    Exception("No existe una sesión activa.")
                )
            }

        return when (user.role) {

            UserRole.ADMIN ->
                repository.getAllPayments()

            UserRole.PARTNER ->
                repository.getPaymentByEmail(user.email)
        } as Result<List<Payment>>
    }
}