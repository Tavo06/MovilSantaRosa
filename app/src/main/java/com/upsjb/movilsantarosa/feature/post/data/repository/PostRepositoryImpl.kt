package com.upsjb.movilsantarosa.feature.post.data.repository

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.upsjb.movilsantarosa.feature.post.data.model.PostModel
import com.upsjb.movilsantarosa.feature.post.domain.model.Post
import com.upsjb.movilsantarosa.feature.post.domain.model.toDomain
import com.upsjb.movilsantarosa.feature.post.domain.model.toModel
import com.upsjb.movilsantarosa.feature.post.domain.repository.PostRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

const val POST_DATABASE = "post_database"

class PostRepositoryImpl @Inject constructor(
    private val database: FirebaseDatabase
) : PostRepository {
    override fun getAllPosts(): Flow<List<Post>> =
        callbackFlow {

            val ref = database.reference.child(POST_DATABASE)

            val listener = object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {

                    val posts = snapshot.children.mapNotNull {
                        it.getValue(PostModel::class.java)
                    }.map(PostModel::toDomain)

                    trySend(posts)
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

    override suspend fun getPostById(id: String): Result<Post> {
        return try {

            val snapshot = database.reference
                .child(POST_DATABASE)
                .child(id)
                .get()
                .await()

            if (!snapshot.exists()) {
                return Result.failure(Exception("Publicación no encontrada"))
            }

            val model = snapshot.getValue(PostModel::class.java)
                ?: return Result.failure(Exception("Error al parsear la publicación"))

            Result.success(model.toDomain())

        } catch (e: Exception) {
            Result.failure(
                Exception(e.message ?: "Error al obtener la publicación")
            )
        }
    }

    override suspend fun registerPost(
        post: Post
    ): Result<Unit> {

        return try {

            val id = post.id.ifBlank {
                database.reference
                    .child(POST_DATABASE)
                    .push()
                    .key
                    ?: throw Exception("No se pudo generar el identificador.")
            }

            database.reference
                .child(POST_DATABASE)
                .child(id)
                .setValue(
                    post.copy(id = id).toModel()
                )
                .await()

            Result.success(Unit)

        } catch (e: Exception) {

            Result.failure(
                Exception(e.message ?: "No se pudo registrar la publicación.")
            )
        }
    }

    override suspend fun updatePost(
        post: Post
    ): Result<Unit> {

        return try {

            database.reference
                .child(POST_DATABASE)
                .child(post.id)
                .setValue(post.toModel())
                .await()

            Result.success(Unit)

        } catch (e: Exception) {

            Result.failure(
                Exception(e.message ?: "Error al actualizar la publicación")
            )
        }
    }

    override suspend fun deletePost(id: String): Result<Unit> {
        return try {

            database.reference
                .child(POST_DATABASE)
                .child(id)
                .removeValue()
                .await()

            Result.success(Unit)

        } catch (e: Exception) {

            Result.failure(
                Exception(e.message ?: "No se pudo eliminar la publicación.")
            )
        }
    }
}