package com.upsjb.movilsantarosa.ui.feature.members.di

import com.google.firebase.database.FirebaseDatabase
import com.upsjb.movilsantarosa.data.member.repository.MemberRepositoryImpl
import com.upsjb.movilsantarosa.domain.member.repository.MemberRepository
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