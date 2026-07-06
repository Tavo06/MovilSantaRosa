package com.upsjb.movilsantarosa.feature.fine.domain.usecase

import com.upsjb.movilsantarosa.feature.fine.domain.model.Fine
import com.upsjb.movilsantarosa.feature.fine.domain.repository.FineRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFinesByEmailUseCase @Inject constructor(
    private val repository: FineRepository
) {

    operator fun invoke(email: String): Flow<List<Fine>> {
        return repository.getFinesByEmail(email)
    }
}