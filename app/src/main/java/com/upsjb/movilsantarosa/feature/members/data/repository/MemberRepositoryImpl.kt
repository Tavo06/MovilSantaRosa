package com.upsjb.movilsantarosa.feature.members.data.repository

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.upsjb.movilsantarosa.feature.auth.domain.model.UserStatus
import com.upsjb.movilsantarosa.feature.members.data.model.MemberModel
import com.upsjb.movilsantarosa.feature.members.domain.model.Member
import com.upsjb.movilsantarosa.feature.members.domain.model.toDomain
import com.upsjb.movilsantarosa.feature.members.domain.repository.MemberRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

const val USER_DATABASE = "user_database"

class MemberRepositoryImpl @Inject constructor(
    val database: FirebaseDatabase
) : MemberRepository {
    override fun getAllMembers(): Flow<List<Member>> = callbackFlow {

        val reference = database.reference.child(USER_DATABASE)

        val listener = object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                val members = snapshot.children.mapNotNull { child ->
                    child.getValue(MemberModel::class.java)
                        ?.toDomain()
                        ?.copy(uid = child.key.orEmpty())
                }
                    .sortedBy { it.lastname }

                trySend(members).isSuccess
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }

        reference.addValueEventListener(listener)

        awaitClose {
            reference.removeEventListener(listener)
        }
    }

    override suspend fun updateMemberStatus(
        uid: String,
        status: UserStatus
    ): Result<Unit> {
        return try {

            database.reference
                .child(USER_DATABASE)
                .child(uid)
                .child("status")
                .setValue(status)
                .await()

            Result.success(Unit)

        } catch (e: Exception) {
            Result.failure(
                Exception(e.message ?: "Error al actualizar el estado del socio")
            )
        }
    }

    override suspend fun updateMember(member: Member): Result<Unit> {
        return try {

            val updates = mapOf(
                "firstname" to member.firstname,
                "lastname" to member.lastname,
                "dniNumber" to member.dniNumber,
                "birthdate" to member.birthdate,
                "phone" to member.phone,
                "plateNumber" to member.plateNumber,
                "licenceNumber" to member.licenceNumber,
                "vehicleColor" to member.vehicleColor
            )

            database.reference
                .child(USER_DATABASE)
                .child(member.uid)
                .updateChildren(updates)
                .await()

            Result.success(Unit)

        } catch (e: Exception) {
            Result.failure(
                Exception(e.message ?: "Error al actualizar los datos del socio")
            )
        }
    }
}