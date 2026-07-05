package com.upsjb.movilsantarosa.feature.fine.domain.usecase

import com.upsjb.movilsantarosa.feature.auth.domain.model.UserRole
import com.upsjb.movilsantarosa.feature.auth.domain.usecase.CurrentUserUseCase
import com.upsjb.movilsantarosa.feature.fine.domain.model.Fine
import com.upsjb.movilsantarosa.feature.fine.domain.repository.FineRepository
import javax.inject.Inject

class GetFinesUseCase @Inject constructor(
    private val repository: FineRepository,
    private val currentUserUseCase: CurrentUserUseCase
) {

    suspend operator fun invoke(): Result<List<Fine>> {

        val user = currentUserUseCase()
            .getOrElse {
                return Result.failure(
                    Exception("No existe una sesión activa.")
                )
            }

        return when (user.rol) {

            UserRole.ADMIN ->
                repository.getAllFines()

            UserRole.PARTNER ->
                repository.getFinesByEmail(user.email)
        }
    }
}