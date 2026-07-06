package com.upsjb.movilsantarosa.feature.home.di

import com.google.firebase.database.FirebaseDatabase
import com.upsjb.movilsantarosa.feature.home.domain.HomeRepository
import com.upsjb.movilsantarosa.feature.home.domain.HomeRepositoryImpl
import com.upsjb.movilsantarosa.feature.post.data.repository.PostRepositoryImpl
import com.upsjb.movilsantarosa.feature.post.domain.repository.PostRepository
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