package com.upsjb.movilsantarosa.feature.home.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.upsjb.movilsantarosa.feature.home.ui.components.AnnouncementsPreviewCard
import com.upsjb.movilsantarosa.feature.home.ui.components.CommunityCard
import com.upsjb.movilsantarosa.feature.home.ui.components.PendingRegistrationsSection
import com.upsjb.movilsantarosa.feature.home.ui.components.PersonalStatusCard
import com.upsjb.movilsantarosa.feature.home.ui.components.SummaryStatsSection
import com.upsjb.movilsantarosa.feature.home.ui.components.TypeStat
import com.upsjb.movilsantarosa.feature.home.ui.components.WelcomeSection

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onOpenPendingRequests: () -> Unit = {},
    onOpenProfile: () -> Unit = {},
    onOpenMembers: () -> Unit = {},
    onOpenPosts: () -> Unit = {},
    onOpenFines: () -> Unit = {},
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val isAdmin = (uiState.userUiState as? UserUiState.Success)?.user?.role == UserRole.ADMIN
    val stats = uiState.statsUiState as? StatsUiState.Success
    val activeFineCount = uiState.financialStatus?.activeFineCount ?: 0

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

        PersonalStatusCard(
            activeFineCount = activeFineCount,
            onClick = onOpenProfile
        )

        if (isAdmin) {

            SummaryStatsSection(
                statsUiState = uiState.statsUiState,
                onStatClick = { type ->
                    when (type) {
                        TypeStat.ACTIVE_MEMBERS,
                        TypeStat.PAYMENTS_ON_TIME -> onOpenMembers()

                        TypeStat.DEBTORS -> onOpenFines()

                        TypeStat.PENDING_FINES -> onOpenPosts()
                    }
                }
            )

            PendingRegistrationsSection(
                pendingCount = stats?.pendingRegistrations ?: 0,
                onViewRequestsClick = onOpenPendingRequests
            )

        } else {

            CommunityCard(
                activeMembersCount = stats?.activeMembers ?: 0,
                onClick = onOpenMembers,
                modifier = Modifier.fillMaxWidth()
            )

            AnnouncementsPreviewCard(
                totalCount = stats?.totalPostCount ?: 0,
                latestTitle = stats?.latestPostTitle,
                onClick = onOpenPosts,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
