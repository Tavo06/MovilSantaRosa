package com.upsjb.movilsantarosa.feature.fine.domain.usecase

import com.upsjb.movilsantarosa.feature.auth.domain.model.UserRole
import com.upsjb.movilsantarosa.feature.auth.domain.usecase.CurrentUserUseCase
import com.upsjb.movilsantarosa.feature.fine.domain.model.Fine
import com.upsjb.movilsantarosa.feature.fine.domain.repository.FineRepository
import javax.inject.Inject

class RegisterFineUseCase @Inject constructor(
    private val repository: FineRepository,
    private val currentUserUseCase: CurrentUserUseCase
) {

    suspend operator fun invoke(fine: Fine): Result<Unit> {

        val user = currentUserUseCase()
            .getOrElse {
                return Result.failure(
                    Exception("No existe una sesión activa.")
                )
            }

        if (user.rol != UserRole.ADMIN) {
            return Result.failure(
                Exception("No tienes permisos para registrar multas.")
            )
        }

        val fineToSave = fine.copy(
            createdBy = user.email,
            createdAt = fine.createdAt.ifBlank {
                System.currentTimeMillis().toString()
            }
        )

        return repository.registerFine(fineToSave)
    }
}