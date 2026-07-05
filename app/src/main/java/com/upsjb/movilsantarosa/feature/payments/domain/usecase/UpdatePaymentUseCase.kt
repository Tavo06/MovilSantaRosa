package com.upsjb.movilsantarosa.feature.payments.domain.usecase

import com.upsjb.movilsantarosa.feature.auth.domain.model.UserRole
import com.upsjb.movilsantarosa.feature.auth.domain.usecase.CurrentUserUseCase
import com.upsjb.movilsantarosa.feature.fine.domain.model.Fine
import com.upsjb.movilsantarosa.feature.fine.domain.repository.FineRepository
import com.upsjb.movilsantarosa.feature.payments.domain.model.Payment
import com.upsjb.movilsantarosa.feature.payments.domain.repository.PaymentRepository
import javax.inject.Inject

class UpdatePaymentUseCase @Inject constructor(
    private val repository: PaymentRepository,
    private val currentUserUseCase: CurrentUserUseCase
) {

    suspend operator fun invoke(payment: Payment): Result<Unit> {

        val user = currentUserUseCase()
            .getOrElse {
                return Result.failure(
                    Exception("No existe una sesión activa.")
                )
            }

        if (user.rol != UserRole.ADMIN) {
            return Result.failure(
                Exception("No tienes permisos para actualizar pagos.")
            )
        }

        val paymentToUpdate = payment.copy(
            createdBy = payment.createdBy.ifBlank { user.email },
            createdAt = payment.createdAt.ifBlank {
                System.currentTimeMillis().toString()
            }
        )

        return repository.updatePayment(paymentToUpdate)
    }
}