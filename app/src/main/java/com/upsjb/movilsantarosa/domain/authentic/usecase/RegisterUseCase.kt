package com.upsjb.movilsantarosa.domain.authentic.usecase

import com.upsjb.movilsantarosa.domain.authentic.model.User
import com.upsjb.movilsantarosa.domain.authentic.repository.AuthRepository
import com.upsjb.movilsantarosa.domain.authentic.request.RegisterRequest
import javax.inject.Inject

class RegisterUseCase @Inject constructor(private val repository: AuthRepository) {
    suspend operator fun invoke(registerRequest: RegisterRequest): Result<User> {
        return repository.register(registerRequest)
    }
}