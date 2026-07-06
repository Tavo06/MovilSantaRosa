package com.upsjb.movilsantarosa.feature.post.domain.usecase

import com.upsjb.movilsantarosa.feature.post.domain.model.Location
import com.upsjb.movilsantarosa.feature.post.domain.repository.LocationRepository
import javax.inject.Inject

class GetCurrentLocationUseCase @Inject constructor(
    private val repository: LocationRepository
) {

    suspend operator fun invoke(): Result<Location> {
        return repository.getCurrentLocation()
    }
}