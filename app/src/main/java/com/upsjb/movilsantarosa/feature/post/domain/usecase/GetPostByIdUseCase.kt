package com.upsjb.movilsantarosa.feature.post.domain.usecase

import com.upsjb.movilsantarosa.feature.post.domain.model.Post
import com.upsjb.movilsantarosa.feature.post.domain.repository.PostRepository
import javax.inject.Inject

class GetPostByIdUseCase @Inject constructor(
    private val repository: PostRepository
) {
    suspend operator fun invoke(
        id: String
    ): Result<Post> {
        return repository.getPostById(id)
    }
}