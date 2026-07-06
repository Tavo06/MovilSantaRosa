package com.upsjb.movilsantarosa.feature.post.domain.repository

import com.upsjb.movilsantarosa.feature.post.domain.model.Location

interface LocationRepository {

    suspend fun getCurrentLocation(): Result<Location>
    suspend fun getAddress(
        latitude: Double,
        longitude: Double
    ): Result<String>
}