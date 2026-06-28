package com.upsjb.movilsantarosa.domain.authentic.repository

import com.upsjb.movilsantarosa.domain.authentic.model.User
import com.upsjb.movilsantarosa.domain.authentic.request.RegisterRequest

interface AuthRepository {
    suspend fun login(
        email: String,
        password: String
    ): Result<User>

    suspend fun logout(): Result<Unit>
    suspend fun register(
        registerRequest: RegisterRequest
    ): Result<User>
    val currentUser: User?
}
