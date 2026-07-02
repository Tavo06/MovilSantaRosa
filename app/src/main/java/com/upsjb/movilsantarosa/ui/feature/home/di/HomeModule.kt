package com.upsjb.movilsantarosa.ui.feature.home.di

import com.google.firebase.database.FirebaseDatabase
import com.upsjb.movilsantarosa.data.home.repository.HomeRepositoryImpl
import com.upsjb.movilsantarosa.domain.home.repository.HomeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object HomeModule {

    @Provides
    @Singleton
    fun provideHomeRepository(
        database: FirebaseDatabase
    ): HomeRepository =
        HomeRepositoryImpl(database)

}