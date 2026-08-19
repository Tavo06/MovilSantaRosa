package com.upsjb.movilsantarosa.feature.post.ui.post

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upsjb.movilsantarosa.core.utils.currentTimeMillis
import com.upsjb.movilsantarosa.feature.auth.domain.model.UserRole
import com.upsjb.movilsantarosa.feature.auth.domain.usecase.CurrentUserUseCase
import com.upsjb.movilsantarosa.feature.post.domain.usecase.DeletePostUseCase
import com.upsjb.movilsantarosa.feature.post.domain.usecase.GetAllPostsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val getAllPostsUseCase: GetAllPostsUseCase,
    private val currentUserUseCase: CurrentUserUseCase,
    private val deletePostUseCase: DeletePostUseCase
) : ViewModel() {

    private val filter = MutableStateFlow(PostFilter.ACTIVE)

    val uiState: StateFlow<PostUiState> = flow {

        val isAdmin = currentUserUseCase()
            .getOrNull()
            ?.role == UserRole.ADMIN

        emitAll(
            combine(getAllPostsUseCase(), filter) { posts, currentFilter ->

                val now = currentTimeMillis()

                val filtered = when (currentFilter) {
                    PostFilter.ALL -> posts
                    PostFilter.ACTIVE -> posts.filter { it.expiredAt > now }
                    PostFilter.EXPIRED -> posts.filter { it.expiredAt <= now }
                }

                PostUiState.Success(
                    posts = filtered.sortedByDescending { it.createdAt },
                    filter = currentFilter,
                    isAdmin = isAdmin
                )
            }
        )
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = PostUiState.Loading
        )

    private val _actionState = MutableStateFlow<PostActionState>(PostActionState.Idle)
    val actionState: StateFlow<PostActionState> = _actionState.asStateFlow()

    fun updateFilter(newFilter: PostFilter) {
        filter.value = newFilter
    }

    fun deletePost(id: String) {
        viewModelScope.launch {

            _actionState.value = PostActionState.Loading

            deletePostUseCase(id)
                .onSuccess {
                    _actionState.value = PostActionState.Success
                }
                .onFailure { error ->
                    _actionState.value = PostActionState.Error(
                        error.message ?: "No se pudo eliminar la publicación."
                    )
                }
        }
    }

    fun resetActionState() {
        _actionState.value = PostActionState.Idle
    }
}
