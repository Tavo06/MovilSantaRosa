package com.upsjb.movilsantarosa.feature.home.domain

interface HomeRepository {

    suspend fun getHomeStats(): Result<HomeStats>

}