package com.upsjb.movilsantarosa.feature.post.ui.post

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
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
    val actionState by viewModel.actionState.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .padding(start = 16.dp, end = 16.dp, top = 16.dp)
            .fillMaxSize()
    ) {

        when (val state = uiState) {

            PostUiState.Loading -> {
                SkeletonSection(modifier = Modifier.fillMaxSize())
            }

            is PostUiState.Error -> {
                ErrorSection(
                    modifier = Modifier.fillMaxSize(),
                    description = state.message,
                    title = "Ups, tenemos incovenientes"
                )
            }

            is PostUiState.Success -> {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    PostFilter.entries.forEach { filterOption ->
                        FilterChip(
                            selected = state.filter == filterOption,
                            onClick = { viewModel.updateFilter(filterOption) },
                            label = { Text(filterOption.displayName) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                PostList(
                    posts = state.posts,
                    onClick = onPostClick,
                    onViewMapClick = onViewMapClick,
                    isAdmin = state.isAdmin,
                    actionState = actionState,
                    onDeleteConfirmed = { post -> viewModel.deletePost(post.id) },
                    onResetAction = { viewModel.resetActionState() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
            }
        }
    }
}
