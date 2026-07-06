package com.upsjb.movilsantarosa.feature.post.di

import com.google.firebase.database.FirebaseDatabase
import com.upsjb.movilsantarosa.feature.post.data.repository.PostRepositoryImpl
import com.upsjb.movilsantarosa.feature.post.domain.repository.PostRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PostModule {

    @Provides
    @Singleton
    fun providePostRepository(
        database: FirebaseDatabase
    ): PostRepository =
        PostRepositoryImpl(database)

}