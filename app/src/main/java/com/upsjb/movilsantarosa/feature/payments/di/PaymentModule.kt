package com.upsjb.movilsantarosa.feature.payments.di

import com.google.firebase.database.FirebaseDatabase
import com.upsjb.movilsantarosa.feature.payments.data.repository.PaymentRepositoryImpl
import com.upsjb.movilsantarosa.feature.payments.domain.repository.PaymentRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PaymentModule {

    @Provides
    @Singleton
    fun providePaymentRepository(
        database: FirebaseDatabase
    ): PaymentRepository =
        PaymentRepositoryImpl(database)

}