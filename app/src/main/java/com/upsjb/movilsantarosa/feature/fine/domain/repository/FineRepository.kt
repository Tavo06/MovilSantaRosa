package com.upsjb.movilsantarosa.feature.fine.domain.repository

import com.upsjb.movilsantarosa.feature.fine.domain.model.Fine
import kotlinx.coroutines.flow.Flow

interface FineRepository {

    fun getFinesByEmail(email: String): Flow<List<Fine>>

    fun getAllFines(): Flow<List<Fine>>

    suspend fun getFineById(
        id: String
    ): Result<Fine>

    suspend fun registerFine(
        fine: Fine
    ): Result<Unit>

    suspend fun updateFine(
        fine: Fine
    ): Result<Unit>
}