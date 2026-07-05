package com.upsjb.movilsantarosa.feature.fine.di

import com.google.firebase.database.FirebaseDatabase
import com.upsjb.movilsantarosa.feature.fine.data.repository.FineRepositoryImpl
import com.upsjb.movilsantarosa.feature.fine.domain.repository.FineRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FineModule {

    @Provides
    @Singleton
    fun provideFineRepository(
        database: FirebaseDatabase
    ): FineRepository =
        FineRepositoryImpl(database)

}