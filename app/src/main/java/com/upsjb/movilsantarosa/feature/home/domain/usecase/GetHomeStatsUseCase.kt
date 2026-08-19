package com.upsjb.movilsantarosa.feature.home.domain.usecase

import com.upsjb.movilsantarosa.core.utils.currentTimeMillis
import com.upsjb.movilsantarosa.core.utils.toDateString
import com.upsjb.movilsantarosa.feature.fine.data.model.FineStatus
import com.upsjb.movilsantarosa.feature.fine.domain.usecase.GetFinesUseCase
import com.upsjb.movilsantarosa.feature.home.domain.model.HomeStats
import com.upsjb.movilsantarosa.feature.members.domain.usecase.GetActiveMembersUseCase
import com.upsjb.movilsantarosa.feature.members.domain.usecase.GetPendingMembersUseCase
import com.upsjb.movilsantarosa.feature.post.domain.usecase.GetAllPostsUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject
import kotlin.collections.count

class GetHomeStatsUseCase @Inject constructor(
    private val getMembersUseCase: GetActiveMembersUseCase,
    private val getFinesUseCase: GetFinesUseCase,
    private val getPostsUseCase: GetAllPostsUseCase,
    private val getPendingMembersUseCase: GetPendingMembersUseCase,
) {

    operator fun invoke(): Flow<HomeStats> {

        return combine(
            getMembersUseCase(),
            getFinesUseCase(),
            getPostsUseCase(),
            getPendingMembersUseCase()
        ) { members, fines, posts, pendingMembers ->

            val totalMembers = members.size

            val totalFines = fines
                .filter { it.status == FineStatus.PENDING }
                .map { it.memberEmail }
                .toSet()
                .size

            val totalPayments = totalMembers - totalFines

            val totalPost = posts.count { post ->
                post.expiredAt > currentTimeMillis()
            }

            HomeStats(
                totalMembers = totalMembers,
                totalFines = totalFines,
                totalPayments = totalPayments,
                totalPost = totalPost,
                pendingRegistrations = pendingMembers.size,
                totalPostCount = posts.size,
                latestPostTitle = posts.maxByOrNull { it.createdAt }?.title
            )
        }
    }
}