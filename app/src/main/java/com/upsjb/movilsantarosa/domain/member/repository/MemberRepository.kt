package com.upsjb.movilsantarosa.domain.member.repository

import com.upsjb.movilsantarosa.domain.member.model.Members

interface MemberRepository {
    suspend fun getAllMembers():
            Result<List<Members>>
}
