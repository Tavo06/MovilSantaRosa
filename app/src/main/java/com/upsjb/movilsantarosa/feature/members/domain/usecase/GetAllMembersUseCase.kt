package com.upsjb.movilsantarosa.feature.members.domain.usecase

import com.upsjb.movilsantarosa.feature.members.domain.model.Member
import com.upsjb.movilsantarosa.feature.members.domain.repository.MemberRepository
import javax.inject.Inject

class GetAllMembersUseCase @Inject constructor(
    private val memberRepository: MemberRepository
) {

    suspend operator fun invoke(): Result<List<Member>> {
        return memberRepository.getAllMembers()
    }

}