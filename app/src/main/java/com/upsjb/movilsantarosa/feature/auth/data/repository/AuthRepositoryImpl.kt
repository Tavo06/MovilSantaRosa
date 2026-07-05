package com.upsjb.movilsantarosa.feature.auth.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.upsjb.movilsantarosa.feature.auth.data.model.AuthModel
import com.upsjb.movilsantarosa.feature.auth.data.model.toUser
import com.upsjb.movilsantarosa.feature.auth.domain.model.User
import com.upsjb.movilsantarosa.feature.auth.domain.model.UserRole
import com.upsjb.movilsantarosa.feature.auth.domain.repository.AuthRepository
import com.upsjb.movilsantarosa.feature.auth.domain.request.RegisterRequest
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

const val USER_DATABASE = "user_database"

class AuthRepositoryImpl @Inject constructor(
    val auth: FirebaseAuth,
    val database: FirebaseDatabase
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
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun register(registerRequest: RegisterRequest): Result<User> {
        return try {
            auth.createUserWithEmailAndPassword(
                registerRequest.email,
                registerRequest.password
            ).await()

            val currentUser = auth.currentUser

            if (currentUser != null) {

                val user = User(
                    firstname = registerRequest.firstname,
                    uid = currentUser.uid,
                    email = currentUser.email.orEmpty()
                )

                database.reference
                    .child(USER_DATABASE)
                    .child(currentUser.uid)
                    .setValue(registerRequest.copy(password = ""))
                    .await()

                Result.success(user)

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
                rol = snapshot.child("rol")
                    .getValue(UserRole::class.java)
                    ?: UserRole.ADMIN
            )

            Result.success(user)

        } catch (e: Exception) {
            Result.failure(
                Exception(e.message ?: "Error al obtener usuario")
            )
        }
    }
}