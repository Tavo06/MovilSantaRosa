package com.upsjb.movilsantarosa.feature.auth.domain.usecase

import com.upsjb.movilsantarosa.feature.auth.domain.model.User
import com.upsjb.movilsantarosa.feature.auth.domain.repository.AuthRepository
import com.upsjb.movilsantarosa.feature.auth.domain.request.RegisterRequest
import javax.inject.Inject

class RegisterUseCase @Inject constructor(private val repository: AuthRepository) {
    suspend operator fun invoke(registerRequest: RegisterRequest): Result<User> {
        return repository.register(registerRequest)
    }
}