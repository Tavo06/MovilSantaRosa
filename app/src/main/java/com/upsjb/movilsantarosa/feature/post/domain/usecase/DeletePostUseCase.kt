package com.upsjb.movilsantarosa.feature.post.domain.usecase

import com.upsjb.movilsantarosa.feature.auth.domain.model.UserRole
import com.upsjb.movilsantarosa.feature.auth.domain.usecase.CurrentUserUseCase
import com.upsjb.movilsantarosa.feature.post.domain.repository.PostRepository
import javax.inject.Inject

class DeletePostUseCase @Inject constructor(
    private val repository: PostRepository,
    private val currentUserUseCase: CurrentUserUseCase
) {

    suspend operator fun invoke(id: String): Result<Unit> {

        val user = currentUserUseCase()
            .getOrElse {
                return Result.failure(
                    Exception("No existe una sesión activa.")
                )
            }

        if (user.role != UserRole.ADMIN) {
            return Result.failure(
                Exception("No tienes permisos para eliminar anuncios.")
            )
        }

        return repository.deletePost(id)
    }
}
