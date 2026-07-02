package com.upsjb.movilsantarosa.data.home.repository


import com.google.firebase.database.FirebaseDatabase
import com.upsjb.movilsantarosa.domain.authentic.model.User
import com.upsjb.movilsantarosa.domain.home.repository.HomeRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

const val USER_DATABASE = "user_database"

class HomeRepositoryImpl @Inject constructor(
    val database: FirebaseDatabase
) : HomeRepository {
    override suspend fun getUser(uid: String): Result<User> {
        return try {

            val snapshot = database.reference
                .child(USER_DATABASE)
                .child(uid)
                .get()
                .await()

            if (!snapshot.exists()) {
                return Result.failure(Exception("Usuario no encontrado"))
            }

            val firstname = snapshot.child("firstname")
                .getValue(String::class.java)
                .orEmpty()

            val email = snapshot.child("email")
                .getValue(String::class.java)
                .orEmpty()

            val rol = snapshot.child("rolUser")
                .getValue(String::class.java)
                .orEmpty()

            Result.success(
                User(
                    firstname = firstname,
                    email = email,
                    uid = uid,
                    rol = rol
                )
            )

        } catch (e: Exception) {
            Result.failure(
                Exception(e.message ?: "Error al obtener el usuario")
            )
        }
    }
}