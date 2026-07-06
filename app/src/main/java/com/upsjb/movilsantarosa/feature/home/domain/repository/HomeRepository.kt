package com.upsjb.movilsantarosa.feature.home.domain.repository

import com.upsjb.movilsantarosa.feature.home.domain.model.HomeStats

interface HomeRepository {

    suspend fun getHomeStats(): Result<HomeStats>

}