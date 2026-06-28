package com.upsjb.movilsantarosa.domain.authentic.usecase

import com.upsjb.movilsantarosa.domain.authentic.model.User
import com.upsjb.movilsantarosa.domain.authentic.repository.AuthRepository

class CurrentUserUseCase(private val repository: AuthRepository) {
    operator fun invoke(): User? {
        return repository.currentUser
    }
}