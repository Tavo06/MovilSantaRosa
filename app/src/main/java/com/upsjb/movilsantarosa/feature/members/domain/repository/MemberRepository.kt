package com.upsjb.movilsantarosa.feature.members.domain.repository

import com.upsjb.movilsantarosa.feature.members.domain.model.Member
import kotlinx.coroutines.flow.Flow

interface MemberRepository {
    fun getAllMembers(): Flow<List<Member>>
}