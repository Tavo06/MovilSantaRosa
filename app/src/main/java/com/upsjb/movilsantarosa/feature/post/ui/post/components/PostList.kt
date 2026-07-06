package com.upsjb.movilsantarosa.feature.post.ui.post.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.upsjb.movilsantarosa.core.uicomponents.EmptySection
import com.upsjb.movilsantarosa.feature.post.domain.model.Post

@Composable
fun PostList(
    posts: List<Post>,
    onClick: (Post) -> Unit,
    modifier: Modifier = Modifier
) {
    if (posts.isEmpty()) {

        EmptySection(
            modifier = modifier.fillMaxSize(),
            title = "No hay posts registrados",
            subtitle = "Aquí aparecerán los posts cuando estén disponibles",
        )

    } else {

        LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                bottom = 88.dp
            ),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            items(posts) { post ->
                PostItem(
                    post = post,
                    onClick = onClick
                )
            }
        }
    }
}