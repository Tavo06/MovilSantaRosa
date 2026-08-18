package com.upsjb.movilsantarosa.feature.members.domain.usecase

import com.upsjb.movilsantarosa.feature.auth.domain.model.UserStatus
import com.upsjb.movilsantarosa.feature.members.domain.model.Member
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetPendingMembersUseCase @Inject constructor(
    private val getAllMembersUseCase: GetAllMembersUseCase
) {

    operator fun invoke(): Flow<List<Member>> {
        return getAllMembersUseCase().map { members ->
            members.filter { it.status == UserStatus.INACTIVE }
        }
    }
}
