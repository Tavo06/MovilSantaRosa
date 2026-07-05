package com.upsjb.movilsantarosa.feature.members.di

import com.google.firebase.database.FirebaseDatabase
import com.upsjb.movilsantarosa.feature.members.data.repository.MemberRepositoryImpl
import com.upsjb.movilsantarosa.feature.members.domain.repository.MemberRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object MemberModule {

    @Provides
    @Singleton
    fun provideMemberRepository(
        database: FirebaseDatabase
    ): MemberRepository =
        MemberRepositoryImpl(database)

}