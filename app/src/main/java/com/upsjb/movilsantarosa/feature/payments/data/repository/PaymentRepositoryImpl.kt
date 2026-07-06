package com.upsjb.movilsantarosa.feature.payments.data.repository

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.upsjb.movilsantarosa.feature.fine.data.model.FineStatus
import com.upsjb.movilsantarosa.feature.fine.data.repository.FINE_DATABASE
import com.upsjb.movilsantarosa.feature.payments.data.model.PaymentModel
import com.upsjb.movilsantarosa.feature.payments.domain.model.Payment
import com.upsjb.movilsantarosa.feature.payments.domain.model.toDomain
import com.upsjb.movilsantarosa.feature.payments.domain.model.toModel
import com.upsjb.movilsantarosa.feature.payments.domain.repository.PaymentRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

const val PAYMENT_DATABASE = "payment_database"

class PaymentRepositoryImpl @Inject constructor(
    private val database: FirebaseDatabase
) : PaymentRepository {

    override fun getPaymentByEmail(email: String): Flow<List<Payment>> =
        callbackFlow {

            val query = database.reference
                .child(PAYMENT_DATABASE)
                .orderByChild("memberEmail")
                .equalTo(email)

            val listener = object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {

                    val payments = snapshot.children.mapNotNull {
                        it.getValue(PaymentModel::class.java)
                    }.map(PaymentModel::toDomain)

                    trySend(payments)
                }

                override fun onCancelled(error: DatabaseError) {
                    close(error.toException())
                }
            }

            query.addValueEventListener(listener)

            awaitClose {
                query.removeEventListener(listener)
            }
        }

    override fun getAllPayments(): Flow<List<Payment>> =
        callbackFlow {

            val ref = database.reference.child(PAYMENT_DATABASE)

            val listener = object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {

                    val payments = snapshot.children.mapNotNull {
                        it.getValue(PaymentModel::class.java)
                    }.map(PaymentModel::toDomain)

                    trySend(payments)
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

    override suspend fun getPaymentById(id: String): Result<Payment> {
        return try {

            val snapshot = database.reference
                .child(PAYMENT_DATABASE)
                .child(id)
                .get()
                .await()

            if (!snapshot.exists()) {
                return Result.failure(Exception("Pago no encontrado"))
            }

            val model = snapshot.getValue(PaymentModel::class.java)
                ?: return Result.failure(Exception("Error al parsear el pago"))

            Result.success(model.toDomain())

        } catch (e: Exception) {
            Result.failure(
                Exception(e.message ?: "Error al obtener el pago")
            )
        }
    }

    override suspend fun registerPayment(
        payment: Payment
    ): Result<Unit> {

        return try {

            val id = payment.id.ifBlank {
                database.reference
                    .child(PAYMENT_DATABASE)
                    .push()
                    .key
                    ?: throw Exception("No se pudo generar el identificador.")
            }

            database.reference
                .child(PAYMENT_DATABASE)
                .child(id)
                .setValue(
                    payment.copy(id = id).toModel()
                )
                .await()

            payment.fineId.takeIf { it.isNotBlank() }?.let { fineId ->

                database.reference
                    .child(FINE_DATABASE)
                    .child(fineId)
                    .child("status")
                    .setValue(FineStatus.PAID)
                    .await()
            }

            Result.success(Unit)

        } catch (e: Exception) {

            Result.failure(
                Exception(e.message ?: "No se pudo registrar el pago.")
            )
        }
    }

    override suspend fun updatePayment(payment: Payment): Result<Unit> {
        return try {

            val model = payment.toModel()

            database.reference
                .child(PAYMENT_DATABASE)
                .child(payment.id)
                .setValue(model)
                .await()

            Result.success(Unit)

        } catch (e: Exception) {
            Result.failure(
                Exception(e.message ?: "Error al actualizar el pago")
            )
        }
    }
}