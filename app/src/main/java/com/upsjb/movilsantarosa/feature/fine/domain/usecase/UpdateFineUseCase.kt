package com.upsjb.movilsantarosa.feature.fine.domain.usecase

import com.upsjb.movilsantarosa.feature.auth.domain.model.UserRole
import com.upsjb.movilsantarosa.feature.auth.domain.usecase.CurrentUserUseCase
import com.upsjb.movilsantarosa.feature.fine.domain.model.Fine
import com.upsjb.movilsantarosa.feature.fine.domain.repository.FineRepository
import javax.inject.Inject

class UpdateFineUseCase @Inject constructor(
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
                Exception("No tienes permisos para actualizar multas.")
            )
        }

        val fineToUpdate = fine.copy(
            createdBy = fine.createdBy.ifBlank { user.email },
            createdAt = fine.createdAt.ifBlank {
                System.currentTimeMillis().toString()
            }
        )

        return repository.updateFine(fineToUpdate)
    }
}