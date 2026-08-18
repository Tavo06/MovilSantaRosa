package com.upsjb.movilsantarosa.feature.auth.domain.usecase

import com.upsjb.movilsantarosa.feature.auth.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveActiveSessionUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    operator fun invoke(uid: String): Flow<String?> {
        return repository.observeActiveSession(uid)
    }
}
