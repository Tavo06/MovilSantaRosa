package com.upsjb.movilsantarosa.feature.auth.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.upsjb.movilsantarosa.core.storage.SessionLocalDataSource
import com.upsjb.movilsantarosa.feature.auth.data.model.AuthModel
import com.upsjb.movilsantarosa.feature.auth.data.model.toUser
import com.upsjb.movilsantarosa.feature.auth.domain.model.User
import com.upsjb.movilsantarosa.feature.auth.domain.model.UserRole
import com.upsjb.movilsantarosa.feature.auth.domain.model.UserStatus
import com.upsjb.movilsantarosa.feature.auth.domain.repository.AuthRepository
import com.upsjb.movilsantarosa.feature.auth.domain.request.RegisterRequest
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject

const val USER_DATABASE = "user_database"

class AuthRepositoryImpl @Inject constructor(
    val auth: FirebaseAuth,
    val database: FirebaseDatabase,
    private val sessionLocalDataSource: SessionLocalDataSource
) : AuthRepository {

    override suspend fun login(
        email: String,
        password: String
    ): Result<User> {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()

            val currentUser = auth.currentUser

            if (currentUser != null) {

                val authModel = AuthModel(
                    currentUser.email.orEmpty(),
                    currentUser.uid
                )

                val user = authModel.toUser()

                val snapshot = database.reference
                    .child(USER_DATABASE)
                    .child(currentUser.uid)
                    .get()
                    .await()

                val firstname = snapshot.child("firstname")
                    .getValue(String::class.java)
                    .orEmpty()

                startNewSession(currentUser.uid)

                Result.success(
                    user.copy(firstname = firstname)
                )

            } else {
                Result.failure(Exception("No se pudo obtener el usuario."))
            }

        } catch (e: Exception) {
            Result.failure(
                Exception(e.message ?: "Usuario o contraseña incorrectos")
            )
        }
    }

    override suspend fun logout(): Result<Unit> {
        return try {
            auth.signOut()
            sessionLocalDataSource.clearSessionId()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun register(registerRequest: RegisterRequest): Result<Unit> {
        return try {
            auth.createUserWithEmailAndPassword(
                registerRequest.email,
                registerRequest.password
            ).await()

            val currentUser = auth.currentUser

            if (currentUser != null) {
                database.reference
                    .child(USER_DATABASE)
                    .child(currentUser.uid)
                    .setValue(registerRequest.copy(password = ""))
                    .await()

                Result.success(Unit)

            } else {
                Result.failure(Exception("No se pudo obtener el usuario registrado."))
            }

        } catch (e: Exception) {
            Result.failure(Exception(e.message ?: "Error al registrar el usuario"))
        }
    }

    override suspend fun getCurrentUser(): Result<User> {
        return try {

            val firebaseUser = auth.currentUser
                ?: return Result.failure(
                    Exception("No hay sesión activa")
                )

            val snapshot = database.reference
                .child(USER_DATABASE)
                .child(firebaseUser.uid)
                .get()
                .await()

            if (!snapshot.exists()) {
                return Result.failure(
                    Exception("Usuario no encontrado en base de datos")
                )
            }

            val user = User(
                uid = firebaseUser.uid,
                email = firebaseUser.email.orEmpty(),
                firstname = snapshot.child("firstname")
                    .getValue(String::class.java)
                    .orEmpty(),
                lastname = snapshot.child("lastname")
                    .getValue(String::class.java)
                    .orEmpty(),
                role = snapshot.child("role")
                    .getValue(UserRole::class.java)
                    ?: UserRole.PARTNER,
                status = snapshot.child("status")
                    .getValue(UserStatus::class.java)
                    ?: UserStatus.ACTIVE
            )

            Result.success(user)

        } catch (e: Exception) {
            Result.failure(
                Exception(e.message ?: "Error al obtener usuario")
            )
        }
    }

    override suspend fun ensureActiveSession(uid: String): Result<Unit> {
        return try {

            if (sessionLocalDataSource.getSessionId() == null) {
                startNewSession(uid)
            }

            Result.success(Unit)

        } catch (e: Exception) {
            Result.failure(
                Exception(e.message ?: "No se pudo establecer la sesión.")
            )
        }
    }

    override fun observeActiveSession(uid: String): Flow<String?> = callbackFlow {

        val ref = database.reference
            .child(USER_DATABASE)
            .child(uid)
            .child("activeSessionId")

        val listener = object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                trySend(snapshot.getValue(String::class.java)).isSuccess
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

    override fun getLocalSessionId(): String? =
        sessionLocalDataSource.getSessionId()

    private suspend fun startNewSession(uid: String) {

        val sessionId = UUID.randomUUID().toString()

        database.reference
            .child(USER_DATABASE)
            .child(uid)
            .child("activeSessionId")
            .setValue(sessionId)
            .await()

        sessionLocalDataSource.saveSessionId(sessionId)
    }
}