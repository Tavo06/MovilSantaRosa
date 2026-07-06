package com.upsjb.movilsantarosa.feature.home.domain


import com.google.firebase.database.FirebaseDatabase
import com.upsjb.movilsantarosa.feature.fine.data.model.FineModel
import com.upsjb.movilsantarosa.feature.post.data.model.PostModel
import com.upsjb.movilsantarosa.feature.post.data.model.PostType
import com.upsjb.movilsantarosa.feature.fine.data.model.FineStatus
import com.upsjb.movilsantarosa.feature.fine.data.repository.FINE_DATABASE
import com.upsjb.movilsantarosa.feature.members.data.model.MemberModel
import com.upsjb.movilsantarosa.feature.members.data.repository.USER_DATABASE
import com.upsjb.movilsantarosa.feature.post.data.repository.POST_DATABASE
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val database: FirebaseDatabase
) : HomeRepository {

    override suspend fun getHomeStats(): Result<HomeStats> {
        return try {

            coroutineScope {

                val membersDeferred = async {
                    database.reference
                        .child(USER_DATABASE)
                        .get()
                        .await()
                }

                val finesDeferred = async {
                    database.reference
                        .child(FINE_DATABASE)
                        .get()
                        .await()
                }

                val postsDeferred = async {
                    database.reference
                        .child(POST_DATABASE)
                        .get()
                        .await()
                }

                val memberSnapshot = membersDeferred.await()
                val fineSnapshot = finesDeferred.await()
                val postSnapshot = postsDeferred.await()

                val members = memberSnapshot.children.mapNotNull {
                    it.getValue(MemberModel::class.java)
                }

                val fines = fineSnapshot.children.mapNotNull {
                    it.getValue(FineModel::class.java)
                }

                val posts = postSnapshot.children.mapNotNull {
                    it.getValue(PostModel::class.java)
                }

                val totalMembers = members.size

                val totalFines = fines
                    .filter { it.status == FineStatus.PENDING }
                    .map { it.memberEmail }
                    .toSet()
                    .size

                val totalPayments = totalMembers - totalFines

                val totalAnnouncements = posts.count {
                    it.type == PostType.ANNOUNCEMENT
                }

                Result.success(
                    HomeStats(
                        totalMembers = totalMembers,
                        totalFines = totalFines,
                        totalPayments = totalPayments,
                        totalAnnouncements = totalAnnouncements
                    )
                )
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}