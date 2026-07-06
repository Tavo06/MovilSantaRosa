package com.upsjb.movilsantarosa.feature.post.data.repository

import com.google.firebase.database.FirebaseDatabase
import com.upsjb.movilsantarosa.feature.post.data.model.PostModel
import com.upsjb.movilsantarosa.feature.post.domain.model.Post
import com.upsjb.movilsantarosa.feature.post.domain.model.toDomain
import com.upsjb.movilsantarosa.feature.post.domain.model.toModel
import com.upsjb.movilsantarosa.feature.post.domain.repository.PostRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

const val POST_DATABASE = "post_database"

class PostRepositoryImpl @Inject constructor(
    private val database: FirebaseDatabase
) : PostRepository {
    override suspend fun getAllPosts(): Result<List<Post>> {
        return try {

            val snapshot = database.reference
                .child(POST_DATABASE)
                .get()
                .await()

            val posts = snapshot.children.mapNotNull {
                it.getValue(PostModel::class.java)
            }.map(PostModel::toDomain)

            Result.success(posts)

        } catch (e: Exception) {

            Result.failure(
                Exception(e.message ?: "No se pudieron obtener los posts.")
            )
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
}