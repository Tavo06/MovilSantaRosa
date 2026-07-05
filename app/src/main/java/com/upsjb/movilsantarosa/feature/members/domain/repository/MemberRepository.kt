package com.upsjb.movilsantarosa.feature.members.domain.repository

import com.upsjb.movilsantarosa.feature.members.domain.model.Member

interface MemberRepository {
    suspend fun getAllMembers():
            Result<List<Member>>
}
