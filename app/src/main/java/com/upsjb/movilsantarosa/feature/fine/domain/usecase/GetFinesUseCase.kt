package com.upsjb.movilsantarosa.feature.fine.domain.usecase

import com.upsjb.movilsantarosa.feature.auth.domain.model.UserRole
import com.upsjb.movilsantarosa.feature.auth.domain.usecase.CurrentUserUseCase
import com.upsjb.movilsantarosa.feature.fine.domain.model.Fine
import com.upsjb.movilsantarosa.feature.fine.domain.repository.FineRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetFinesUseCase @Inject constructor(
    private val repository: FineRepository,
    private val currentUserUseCase: CurrentUserUseCase
) {

    operator fun invoke(): Flow<List<Fine>> {

        return flow {

            val user = currentUserUseCase()
                .getOrElse {
                    throw Exception("No existe una sesión activa.")
                }

            val finesFlow = when (user.role) {

                UserRole.ADMIN ->
                    repository.getAllFines()

                UserRole.PARTNER ->
                    repository.getFinesByEmail(user.email)
            }

            emitAll(finesFlow)
        }
    }
}