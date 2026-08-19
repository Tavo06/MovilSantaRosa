package com.upsjb.movilsantarosa.feature.post.domain.repository

import com.upsjb.movilsantarosa.feature.post.domain.model.Post
import kotlinx.coroutines.flow.Flow

interface PostRepository {

    fun getAllPosts(): Flow<List<Post>>
    suspend fun getPostById(
        id: String
    ): Result<Post>

    suspend fun registerPost(
        post: Post
    ): Result<Unit>

    suspend fun updatePost(
        post: Post
    ): Result<Unit>

    suspend fun deletePost(
        id: String
    ): Result<Unit>
}