package com.upsjb.movilsantarosa.feature.auth.domain.usecase

import com.upsjb.movilsantarosa.feature.auth.domain.model.User
import com.upsjb.movilsantarosa.feature.auth.domain.repository.AuthRepository
import javax.inject.Inject

class CurrentUserUseCase @Inject constructor(
    private val repository: AuthRepository
) {

    suspend operator fun invoke(): Result<User> {
        return repository.getCurrentUser()
    }
}