package com.upsjb.movilsantarosa.feature.home.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.upsjb.movilsantarosa.feature.auth.domain.model.UserRole
import com.upsjb.movilsantarosa.feature.home.ui.components.PendingRegistrationsSection
import com.upsjb.movilsantarosa.feature.home.ui.components.SummaryStatsSection
import com.upsjb.movilsantarosa.feature.home.ui.components.WelcomeSection

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onOpenPendingRequests: () -> Unit = {},
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val isAdmin = (uiState.userUiState as? UserUiState.Success)?.user?.role == UserRole.ADMIN
    val pendingCount = (uiState.statsUiState as? StatsUiState.Success)?.pendingRegistrations ?: 0

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {

        WelcomeSection(
            userUiState = uiState.userUiState,
            onRetry = { viewModel.observeUser() }
        )

        SummaryStatsSection(
            statsUiState = uiState.statsUiState,
        )

        if (isAdmin) {
            PendingRegistrationsSection(
                pendingCount = pendingCount,
                onViewRequestsClick = onOpenPendingRequests
            )
        }
    }
}
