package com.upsjb.movilsantarosa.feature.post.data.repository

import com.google.firebase.database.FirebaseDatabase
import com.upsjb.movilsantarosa.feature.payments.data.model.PaymentModel
import com.upsjb.movilsantarosa.feature.payments.data.repository.PAYMENT_DATABASE
import com.upsjb.movilsantarosa.feature.payments.domain.model.toDomain
import com.upsjb.movilsantarosa.feature.post.data.model.PostModel
import com.upsjb.movilsantarosa.feature.post.domain.model.Post
import com.upsjb.movilsantarosa.feature.post.domain.model.toDomain
import com.upsjb.movilsantarosa.feature.post.domain.model.toModel
import com.upsjb.movilsantarosa.feature.post.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow
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

    /*override suspend fun createPost(post: Post): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun updatePost(post: Post): Result<Unit> {
        TODO("Not yet implemented")
    }
*/
}