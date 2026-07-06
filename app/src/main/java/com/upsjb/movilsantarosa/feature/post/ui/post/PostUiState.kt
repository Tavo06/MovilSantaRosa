package com.upsjb.movilsantarosa.feature.post.ui.post

import com.upsjb.movilsantarosa.feature.post.domain.model.Post

sealed class PostUiState {

    object Loading : PostUiState()

    data class Success(
        val posts: List<Post>
    ) : PostUiState()

    data class Error(
        val message: String
    ) : PostUiState()
}