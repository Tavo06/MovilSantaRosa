package com.upsjb.movilsantarosa.feature.fine.data.repository

import com.google.firebase.database.FirebaseDatabase
import com.upsjb.movilsantarosa.feature.fine.data.model.FineModel
import com.upsjb.movilsantarosa.feature.fine.domain.model.Fine
import com.upsjb.movilsantarosa.feature.fine.domain.model.toDomain
import com.upsjb.movilsantarosa.feature.fine.domain.model.toModel
import com.upsjb.movilsantarosa.feature.fine.domain.repository.FineRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

const val FINE_DATABASE = "fine_database"

class FineRepositoryImpl @Inject constructor(
    private val database: FirebaseDatabase
) : FineRepository {

    override suspend fun getFinesByEmail(
        email: String
    ): Result<List<Fine>> {

        return try {

            val snapshot = database.reference
                .child(FINE_DATABASE)
                .orderByChild("memberEmail")
                .equalTo(email)
                .get()
                .await()

            val fines = snapshot.children.mapNotNull {
                it.getValue(FineModel::class.java)
            }.map(FineModel::toDomain)

            Result.success(fines)

        } catch (e: Exception) {

            Result.failure(
                Exception(e.message ?: "No se pudieron obtener las multas.")
            )
        }
    }

    override suspend fun getAllFines(): Result<List<Fine>> {

        return try {

            val snapshot = database.reference
                .child(FINE_DATABASE)
                .get()
                .await()

            val fines = snapshot.children.mapNotNull {
                it.getValue(FineModel::class.java)
            }.map(FineModel::toDomain)

            Result.success(fines)

        } catch (e: Exception) {

            Result.failure(
                Exception(e.message ?: "No se pudieron obtener las multas.")
            )
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

            val id = if (fine.id.isBlank()) {
                database.reference
                    .child(FINE_DATABASE)
                    .push()
                    .key
                    ?: throw Exception("No se pudo generar el identificador.")
            } else {
                fine.id
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