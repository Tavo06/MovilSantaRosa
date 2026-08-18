package com.upsjb.movilsantarosa.feature.auth.domain.repository

import com.upsjb.movilsantarosa.feature.auth.domain.model.User
import com.upsjb.movilsantarosa.feature.auth.domain.request.RegisterRequest
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun login(
        email: String,
        password: String
    ): Result<User>

    suspend fun logout(): Result<Unit>
    suspend fun register(
        registerRequest: RegisterRequest
    ): Result<Unit>

    suspend fun getCurrentUser(): Result<User>

    suspend fun ensureActiveSession(uid: String): Result<Unit>
    fun observeActiveSession(uid: String): Flow<String?>
    fun getLocalSessionId(): String?
}
