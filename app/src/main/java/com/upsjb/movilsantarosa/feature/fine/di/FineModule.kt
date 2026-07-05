package com.upsjb.movilsantarosa.feature.fine.di

import com.upsjb.movilsantarosa.feature.fine.data.repository.FineRepositoryImpl
import com.upsjb.movilsantarosa.feature.fine.domain.repository.FineRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindFineRepository(
        impl: FineRepositoryImpl
    ): FineRepository
}