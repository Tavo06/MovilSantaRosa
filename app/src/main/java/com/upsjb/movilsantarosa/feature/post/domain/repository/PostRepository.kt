package com.upsjb.movilsantarosa.feature.post.domain.repository

import com.upsjb.movilsantarosa.feature.post.domain.model.Post

interface PostRepository {

    suspend fun getAllPosts(): Result<List<Post>>

    suspend fun createPost(
        post: Post
    ): Result<Unit>

    suspend fun updatePost(
        post: Post
    ): Result<Unit>
}

