package com.upsjb.movilsantarosa.feature.home.domain.usecase

import com.upsjb.movilsantarosa.feature.home.domain.repository.HomeRepository
import com.upsjb.movilsantarosa.feature.auth.domain.model.User
import javax.inject.Inject

class GetUserUseCase @Inject constructor(
    private val repository: HomeRepository
) {
    suspend operator fun invoke(uid: String): Result<User> {
        return repository.getUser(uid)
    }
}