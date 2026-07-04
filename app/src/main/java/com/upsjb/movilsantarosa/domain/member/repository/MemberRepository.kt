package com.upsjb.movilsantarosa.domain.member.repository

import com.upsjb.movilsantarosa.domain.member.model.Member

interface MemberRepository {
    suspend fun getAllMembers():
            Result<List<Member>>
}
