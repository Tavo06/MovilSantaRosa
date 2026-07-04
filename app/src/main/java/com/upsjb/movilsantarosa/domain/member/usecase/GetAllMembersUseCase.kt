package com.upsjb.movilsantarosa.domain.member.usecase

import com.upsjb.movilsantarosa.domain.member.model.Member
import com.upsjb.movilsantarosa.domain.member.repository.MemberRepository
import javax.inject.Inject

class GetAllMembersUseCase @Inject constructor(
    private val memberRepository: MemberRepository
) {

    suspend operator fun invoke(): Result<List<Member>> {
        return memberRepository.getAllMembers()
    }

}