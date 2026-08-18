package com.upsjb.movilsantarosa.feature.post.ui.post

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.upsjb.movilsantarosa.core.uicomponents.ErrorSection
import com.upsjb.movilsantarosa.core.uicomponents.SkeletonSection
import com.upsjb.movilsantarosa.feature.post.domain.model.Post
import com.upsjb.movilsantarosa.feature.post.ui.post.components.PostList

@Composable
fun PostsScreen(
    modifier: Modifier = Modifier,
    onPostClick: (Post) -> Unit,
    onViewMapClick: (Post) -> Unit = {},
    viewModel: PostViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .padding(start = 16.dp, end = 16.dp, top = 16.dp)
            .fillMaxSize()
    ) {

        when (val state = uiState) {

            PostUiState.Loading -> {
                SkeletonSection(
                    modifier = Modifier.fillMaxSize()
                )
            }

            is PostUiState.Error -> {
                ErrorSection(
                    modifier = Modifier.fillMaxSize(),
                    description = state.message,
                    title = "Ups, tenemos incovenientes"
                )
            }

            is PostUiState.Success -> {

                PostList(
                    posts = state.posts,
                    onClick = onPostClick,
                    onViewMapClick = onViewMapClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
            }
        }
    }
}