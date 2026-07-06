package com.upsjb.movilsantarosa.feature.post.domain.usecase

import com.upsjb.movilsantarosa.feature.post.domain.repository.LocationRepository
import javax.inject.Inject

class GetAddressUseCase @Inject constructor(
    private val locationRepository: LocationRepository,
) {
    suspend operator fun invoke(
        latitude: Double,
        longitude: Double
    ): Result<String> {
        return locationRepository.getAddress(
            latitude = latitude,
            longitude = longitude
        )
    }
}