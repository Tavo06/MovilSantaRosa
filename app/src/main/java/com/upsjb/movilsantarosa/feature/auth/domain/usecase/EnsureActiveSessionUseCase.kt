package com.upsjb.movilsantarosa.feature.auth.domain.usecase

import com.upsjb.movilsantarosa.feature.auth.domain.repository.AuthRepository
import javax.inject.Inject

class EnsureActiveSessionUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(uid: String): Result<Unit> {
        return repository.ensureActiveSession(uid)
    }
}
