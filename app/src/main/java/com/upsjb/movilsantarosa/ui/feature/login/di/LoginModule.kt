package com.upsjb.movilsantarosa.ui.feature.login.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.upsjb.movilsantarosa.data.authentic.repository.AuthRepositoryImpl
import com.upsjb.movilsantarosa.domain.authentic.repository.AuthRepository
import com.upsjb.movilsantarosa.domain.authentic.usecase.LoginUseCase
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
        database: FirebaseDatabase
    ): AuthRepository =
        AuthRepositoryImpl(auth, database)

    @Provides
    @Singleton
    fun provideLoginUseCase(
        repository: AuthRepository
    ): LoginUseCase =
        LoginUseCase(repository)
}