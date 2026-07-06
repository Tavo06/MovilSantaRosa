package com.upsjb.movilsantarosa.feature.post.di

import com.upsjb.movilsantarosa.feature.post.data.repository.LocationRepositoryImpl
import com.upsjb.movilsantarosa.feature.post.domain.repository.LocationRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class LocationModule {

    @Binds
    abstract fun bindLocationRepository(
        impl: LocationRepositoryImpl
    ): LocationRepository
}