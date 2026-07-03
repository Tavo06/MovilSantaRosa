package com.upsjb.movilsantarosa.ui.feature.members

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun MemberRoute(
    modifier: Modifier = Modifier,
    viewModel: MemberViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    MembersScreen(
        modifier = modifier,
        uiState = uiState,
        onMemberClick = {
            // Navegar al detalle si lo necesitas
        },

    )
}