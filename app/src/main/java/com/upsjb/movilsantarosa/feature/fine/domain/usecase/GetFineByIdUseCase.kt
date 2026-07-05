package com.upsjb.movilsantarosa.feature.fine.domain.usecase

import com.upsjb.movilsantarosa.feature.fine.domain.model.Fine
import com.upsjb.movilsantarosa.feature.fine.domain.repository.FineRepository
import javax.inject.Inject

class GetFineByIdUseCase @Inject constructor(
    private val repository: FineRepository
) {
    suspend operator fun invoke(id: String): Result<Fine> {
        return repository.getFineById(id)
    }
}