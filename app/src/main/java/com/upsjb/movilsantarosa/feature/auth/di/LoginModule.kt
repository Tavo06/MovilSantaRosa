package com.upsjb.movilsantarosa.feature.auth.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.upsjb.movilsantarosa.core.storage.SessionLocalDataSource
import com.upsjb.movilsantarosa.feature.auth.data.repository.AuthRepositoryImpl
import com.upsjb.movilsantarosa.feature.auth.domain.repository.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object LoginModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth =
        FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseDatabase(): FirebaseDatabase =
        FirebaseDatabase.getInstance()

    @Provides
    @Singleton
    fun provideAuthRepository(
        auth: FirebaseAuth,
        database: FirebaseDatabase,
        sessionLocalDataSource: SessionLocalDataSource
    ): AuthRepository =
        AuthRepositoryImpl(auth, database, sessionLocalDataSource)

}