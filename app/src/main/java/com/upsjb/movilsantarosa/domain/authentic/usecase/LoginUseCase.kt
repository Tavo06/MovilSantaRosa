package com.upsjb.movilsantarosa.domain.authentic.usecase

import com.upsjb.movilsantarosa.domain.authentic.model.User
import com.upsjb.movilsantarosa.domain.authentic.repository.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(private val repository: AuthRepository) {
    suspend operator fun invoke(email: String, password: String): Result<User> {
        return repository.login(email, password)
    }
}