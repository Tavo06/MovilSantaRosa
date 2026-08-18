package com.upsjb.movilsantarosa.feature.payments.domain.usecase

import com.upsjb.movilsantarosa.feature.auth.domain.model.UserRole
import com.upsjb.movilsantarosa.feature.auth.domain.usecase.CurrentUserUseCase
import com.upsjb.movilsantarosa.feature.fine.data.model.FineStatus
import com.upsjb.movilsantarosa.feature.fine.domain.usecase.GetFineByIdUseCase
import com.upsjb.movilsantarosa.feature.payments.domain.model.Payment
import com.upsjb.movilsantarosa.feature.payments.domain.repository.PaymentRepository
import javax.inject.Inject

class RegisterPaymentUseCase @Inject constructor(
    private val repository: PaymentRepository,
    private val currentUserUseCase: CurrentUserUseCase,
    private val getFineByIdUseCase: GetFineByIdUseCase
) {

    suspend operator fun invoke(payment: Payment): Result<Unit> {

        val user = currentUserUseCase()
            .getOrElse {
                return Result.failure(
                    Exception("No existe una sesión activa.")
                )
            }

        if (user.role != UserRole.ADMIN) {
            return Result.failure(
                Exception("No tienes permisos para registrar pagos.")
            )
        }

        if (payment.fineId.isNotBlank()) {

            val fine = getFineByIdUseCase(payment.fineId)
                .getOrElse {
                    return Result.failure(
                        Exception("No se encontró la multa asociada al pago.")
                    )
                }

            if (fine.status != FineStatus.PENDING) {
                return Result.failure(
                    Exception("Esta multa ya no está disponible para el pago.")
                )
            }
        }

        val paymentToSave = payment.copy(
            createdBy = user.email,
            createdAt = payment.createdAt
        )

        return repository.registerPayment(paymentToSave)
    }
}