package com.upsjb.movilsantarosa.feature.home.domain.usecase

import com.upsjb.movilsantarosa.feature.home.domain.HomeRepository
import com.upsjb.movilsantarosa.feature.home.domain.HomeStats
import javax.inject.Inject

class GetHomeStatsUseCase @Inject constructor(
    private val repository: HomeRepository
) {

    suspend operator fun invoke(): Result<HomeStats> {
        return repository.getHomeStats()
    }

}