package com.upsjb.movilsantarosa.domain.authentic.usecase

import com.upsjb.movilsantarosa.domain.authentic.repository.AuthRepository

class LogoutUseCase (private val repository: AuthRepository){
    suspend operator fun invoke(): Result<Unit>{
        return repository.logout()
    }
}