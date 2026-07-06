package com.upsjb.movilsantarosa.feature.fine.domain.usecase

import com.upsjb.movilsantarosa.feature.fine.domain.model.Fine
import com.upsjb.movilsantarosa.feature.fine.domain.repository.FineRepository
import javax.inject.Inject

class GetFinesByEmailUseCase @Inject constructor(
    private val repository: FineRepository
) {

    suspend operator fun invoke(
        email: String
    ): Result<List<Fine>> {
        return repository.getFinesByEmail(email)
    }
}