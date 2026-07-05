package com.upsjb.movilsantarosa.feature.fine.domain.repository

import com.upsjb.movilsantarosa.feature.fine.domain.model.Fine

interface FineRepository {

    suspend fun getFinesByEmail(
        email: String
    ): Result<List<Fine>>

    suspend fun getAllFines(): Result<List<Fine>>

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