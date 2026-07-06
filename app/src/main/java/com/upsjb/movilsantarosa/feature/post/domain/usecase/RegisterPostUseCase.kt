package com.upsjb.movilsantarosa.feature.post.domain.usecase

import com.upsjb.movilsantarosa.feature.post.domain.model.Post
import com.upsjb.movilsantarosa.feature.post.domain.repository.PostRepository
import javax.inject.Inject

class RegisterPostUseCase @Inject constructor(
    private val repository: PostRepository
) {
    suspend operator fun invoke(
        post: Post
    ): Result<Unit> {
        return repository.registerPost(post)
    }
}