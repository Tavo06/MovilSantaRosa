package com.upsjb.movilsantarosa.ui.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.upsjb.movilsantarosa.ui.feature.home.components.HomeHeader
import com.upsjb.movilsantarosa.ui.feature.home.components.SummaryStatsSection
import com.upsjb.movilsantarosa.ui.feature.home.components.WelcomeSection

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    uiState: HomeUIState,
    onLogout: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.onPrimary)
    ) {
        HomeHeader(onLogout = onLogout)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            if (uiState is HomeUIState.Success) {
                WelcomeSection(
                    userName = uiState.user.firstname,
                    userRole = uiState.user.rol
                )
            }

            SummaryStatsSection(
                activeMembers = 20,
                debtors = 5,
                paymentsOnTime = 15,
                pendingFines = 3
            )
        }

    }
}

