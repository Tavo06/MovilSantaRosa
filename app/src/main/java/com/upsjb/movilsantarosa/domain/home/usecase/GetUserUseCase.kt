package com.upsjb.movilsantarosa.domain.home.usecase

import com.upsjb.movilsantarosa.domain.authentic.model.User
import com.upsjb.movilsantarosa.domain.home.repository.HomeRepository
import javax.inject.Inject

class GetUserUseCase @Inject constructor(
    private val repository: HomeRepository
) {
    suspend operator fun invoke(uid: String): Result<User> {
        return repository.getUser(uid)
    }
}