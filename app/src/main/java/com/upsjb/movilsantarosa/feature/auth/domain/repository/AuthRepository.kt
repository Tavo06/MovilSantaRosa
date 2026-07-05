package com.upsjb.movilsantarosa.feature.auth.domain.repository

import com.upsjb.movilsantarosa.feature.auth.domain.model.User
import com.upsjb.movilsantarosa.feature.auth.domain.request.RegisterRequest

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
