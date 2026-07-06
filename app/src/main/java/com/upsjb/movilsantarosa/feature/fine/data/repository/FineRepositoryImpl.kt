package com.upsjb.movilsantarosa.feature.fine.data.repository

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.upsjb.movilsantarosa.feature.fine.data.model.FineModel
import com.upsjb.movilsantarosa.feature.fine.domain.model.Fine
import com.upsjb.movilsantarosa.feature.fine.domain.model.toDomain
import com.upsjb.movilsantarosa.feature.fine.domain.model.toModel
import com.upsjb.movilsantarosa.feature.fine.domain.repository.FineRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

const val FINE_DATABASE = "fine_database"

class FineRepositoryImpl @Inject constructor(
    private val database: FirebaseDatabase,
) : FineRepository {

    override fun getFinesByEmail(email: String): Flow<List<Fine>> = callbackFlow {

        val ref = database.reference
            .child(FINE_DATABASE)
            .orderByChild("memberEmail")
            .equalTo(email)

        val listener = object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                val fines = snapshot.children.mapNotNull {
                    it.getValue(FineModel::class.java)
                }.map(FineModel::toDomain)

                trySend(fines).isSuccess
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }

        ref.addValueEventListener(listener)

        awaitClose {
            ref.removeEventListener(listener)
        }
    }

    override fun getAllFines(): Flow<List<Fine>> = callbackFlow {

        val ref = database.reference.child(FINE_DATABASE)

        val listener = object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                val fines = snapshot.children.mapNotNull {
                    it.getValue(FineModel::class.java)
                }.map(FineModel::toDomain)

                trySend(fines).isSuccess
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }

        ref.addValueEventListener(listener)

        awaitClose {
            ref.removeEventListener(listener)
        }
    }

    override suspend fun getFineById(id: String): Result<Fine> {
        return try {

            val snapshot = database.reference
                .child(FINE_DATABASE)
                .child(id)
                .get()
                .await()

            if (!snapshot.exists()) {
                return Result.failure(Exception("Multa no encontrada"))
            }

            val model = snapshot.getValue(FineModel::class.java)
                ?: return Result.failure(Exception("Error al parsear la multa"))

            Result.success(model.toDomain())

        } catch (e: Exception) {
            Result.failure(
                Exception(e.message ?: "Error al obtener la multa")
            )
        }
    }

    override suspend fun registerFine(
        fine: Fine
    ): Result<Unit> {

        return try {

            val id = fine.id.ifBlank {
                database.reference
                    .child(FINE_DATABASE)
                    .push()
                    .key
                    ?: throw Exception("No se pudo generar el identificador.")
            }

            database.reference
                .child(FINE_DATABASE)
                .child(id)
                .setValue(
                    fine.copy(id = id).toModel()
                )
                .await()

            Result.success(Unit)

        } catch (e: Exception) {

            Result.failure(
                Exception(e.message ?: "No se pudo registrar la multa.")
            )
        }
    }

    override suspend fun updateFine(fine: Fine): Result<Unit> {
        return try {

            val model = fine.toModel()

            database.reference
                .child(FINE_DATABASE)
                .child(fine.id)
                .setValue(model)
                .await()

            Result.success(Unit)

        } catch (e: Exception) {
            Result.failure(
                Exception(e.message ?: "Error al actualizar la multa")
            )
        }
    }
}