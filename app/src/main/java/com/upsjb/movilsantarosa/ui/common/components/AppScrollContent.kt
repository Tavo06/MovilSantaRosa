package com.upsjb.movilsantarosa.ui.common.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun AppScrollContent(
    content: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        content()
    }
}

@Preview(showBackground = true)
@Composable
fun AppScrollContentPreview() {
    AppScrollContent(
        content = {
            repeat(10) {
                androidx.compose.material3.Text("Elemento $it")
            }
        }
    )
}