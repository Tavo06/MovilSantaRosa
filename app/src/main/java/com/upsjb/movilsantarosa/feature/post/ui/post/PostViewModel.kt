package com.upsjb.movilsantarosa.feature.post.ui.post

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upsjb.movilsantarosa.feature.post.domain.usecase.GetAllPostsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    getAllPostsUseCase: GetAllPostsUseCase
) : ViewModel() {

    val uiState: StateFlow<PostUiState> =
        getAllPostsUseCase()
            .map { posts ->
                PostUiState.Success(posts = posts)
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = PostUiState.Loading
            )
}