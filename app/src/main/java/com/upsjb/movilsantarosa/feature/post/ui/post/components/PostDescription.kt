package com.upsjb.movilsantarosa.feature.post.ui.post.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PostsDescription(
    modifier: Modifier = Modifier
) {
    Text(
        text = "Aquí se registran los anuncios para los socios.",
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        color = Color.Gray,
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium
    )
}
