package com.upsjb.movilsantarosa.feature.post.ui.post

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upsjb.movilsantarosa.feature.post.domain.usecase.GetAllPostsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val getAllPostsUseCase: GetAllPostsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<PostUiState>(PostUiState.Loading)
    val uiState: StateFlow<PostUiState> = _uiState.asStateFlow()

    init {
        loadPosts()
    }

    fun loadPosts() {
        if (_uiState.value is PostUiState.Success) return

        viewModelScope.launch {
            _uiState.value = PostUiState.Loading

            getAllPostsUseCase()
                .onSuccess { posts ->
                    _uiState.value = PostUiState.Success(
                        posts = posts
                    )
                }
                .onFailure { error ->
                    _uiState.value = PostUiState.Error(
                        error.message ?: "Error al cargar posts"
                    )
                }
        }
    }
}