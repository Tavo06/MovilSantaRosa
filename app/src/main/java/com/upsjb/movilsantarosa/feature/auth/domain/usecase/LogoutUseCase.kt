package com.upsjb.movilsantarosa.feature.auth.domain.usecase

import com.upsjb.movilsantarosa.feature.auth.domain.repository.AuthRepository
import javax.inject.Inject

class LogoutUseCase @Inject constructor(private val repository: AuthRepository) {
    suspend operator fun invoke(): Result<Unit> {
        return repository.logout()
    }
}