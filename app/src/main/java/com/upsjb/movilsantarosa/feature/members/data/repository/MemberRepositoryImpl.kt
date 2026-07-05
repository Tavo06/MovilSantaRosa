package com.upsjb.movilsantarosa.feature.members.data.repository

import com.google.firebase.database.FirebaseDatabase
import com.upsjb.movilsantarosa.feature.members.data.model.MemberModel
import com.upsjb.movilsantarosa.feature.members.domain.model.Member
import com.upsjb.movilsantarosa.feature.members.domain.model.toDomain
import com.upsjb.movilsantarosa.feature.members.domain.repository.MemberRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

const val USER_DATABASE = "user_database"

class MemberRepositoryImpl @Inject constructor(
    val database: FirebaseDatabase
) : MemberRepository {
    override suspend fun getAllMembers(): Result<List<Member>> {
        return try {

            val snapshot = database.reference
                .child(USER_DATABASE)
                .get()
                .await()

            val members = snapshot.children.mapNotNull {
                it.getValue(MemberModel::class.java)
            }.sortedBy { it.lastname }
                .map(MemberModel::toDomain)

            Result.success(members)

        } catch (e: Exception) {

            Result.failure(
                Exception(e.message ?: "Error al obtener los miembros")
            )

        }
    }
}