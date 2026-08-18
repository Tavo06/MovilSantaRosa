package com.upsjb.movilsantarosa.feature.members.domain.usecase

import com.upsjb.movilsantarosa.feature.auth.domain.model.UserRole
import com.upsjb.movilsantarosa.feature.auth.domain.usecase.CurrentUserUseCase
import com.upsjb.movilsantarosa.feature.members.domain.model.Member
import com.upsjb.movilsantarosa.feature.members.domain.repository.MemberRepository
import javax.inject.Inject

class UpdateMemberProfileUseCase @Inject constructor(
    private val repository: MemberRepository,
    private val currentUserUseCase: CurrentUserUseCase
) {

    suspend operator fun invoke(member: Member): Result<Unit> {

        val user = currentUserUseCase()
            .getOrElse {
                return Result.failure(
                    Exception("No existe una sesión activa.")
                )
            }

        if (user.role != UserRole.ADMIN) {
            return Result.failure(
                Exception("No tienes permisos para editar socios.")
            )
        }

        return repository.updateMember(member)
    }
}
