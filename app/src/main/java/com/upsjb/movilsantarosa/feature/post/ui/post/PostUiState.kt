package com.upsjb.movilsantarosa.feature.post.ui.post

import com.upsjb.movilsantarosa.feature.post.domain.model.Post

sealed class PostUiState {

    object Loading : PostUiState()

    data class Success(
        val posts: List<Post>,
        val filter: PostFilter = PostFilter.ACTIVE,
        val isAdmin: Boolean = false
    ) : PostUiState()

    data class Error(
        val message: String
    ) : PostUiState()
}

enum class PostFilter {
    ALL,
    ACTIVE,
    EXPIRED;

    val displayName: String
        get() = when (this) {
            ALL -> "Todos"
            ACTIVE -> "Activos"
            EXPIRED -> "Expirados"
        }
}

sealed interface PostActionState {
    data object Idle : PostActionState
    data object Loading : PostActionState
    data object Success : PostActionState
    data class Error(val message: String) : PostActionState
}
