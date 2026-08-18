package com.upsjb.movilsantarosa.feature.members.domain.repository

import com.upsjb.movilsantarosa.feature.auth.domain.model.UserStatus
import com.upsjb.movilsantarosa.feature.members.domain.model.Member
import kotlinx.coroutines.flow.Flow

interface MemberRepository {
    fun getAllMembers(): Flow<List<Member>>

    suspend fun updateMemberStatus(
        uid: String,
        status: UserStatus
    ): Result<Unit>

    suspend fun updateMember(
        member: Member
    ): Result<Unit>
}