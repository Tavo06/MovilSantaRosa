package com.upsjb.movilsantarosa.domain.member.usecase

import com.upsjb.movilsantarosa.domain.member.model.Members
import com.upsjb.movilsantarosa.domain.member.repository.MemberRepository
import javax.inject.Inject

class GetAllMembersUseCase @Inject constructor(
    private val memberRepository: MemberRepository
) {

    suspend operator fun invoke(): Result<List<Members>> {
        return memberRepository.getAllMembers()
    }

}