package com.upsjb.movilsantarosa.feature.auth.domain.usecase

import com.upsjb.movilsantarosa.feature.auth.domain.repository.AuthRepository
import javax.inject.Inject

class GetLocalSessionIdUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    operator fun invoke(): String? {
        return repository.getLocalSessionId()
    }
}
