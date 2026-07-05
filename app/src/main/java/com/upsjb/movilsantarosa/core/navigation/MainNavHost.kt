package com.upsjb.movilsantarosa.core.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.upsjb.movilsantarosa.core.navigation.component.AnnoucementsDestination
import com.upsjb.movilsantarosa.core.navigation.component.FinesDestination
import com.upsjb.movilsantarosa.core.navigation.component.HomeDestination
import com.upsjb.movilsantarosa.core.navigation.component.MAIN_ROUTES
import com.upsjb.movilsantarosa.core.navigation.component.MembersDestination
import com.upsjb.movilsantarosa.core.navigation.component.Navigator
import com.upsjb.movilsantarosa.core.navigation.component.PaymentsDestination
import com.upsjb.movilsantarosa.core.navigation.component.rememberNavigationState
import com.upsjb.movilsantarosa.core.uicomponents.AppBottomBar
import com.upsjb.movilsantarosa.core.uicomponents.AppFloatingActionButton
import com.upsjb.movilsantarosa.core.uicomponents.AppTopBar
import com.upsjb.movilsantarosa.feature.announcements.AnnouncementsRoute
import com.upsjb.movilsantarosa.feature.fine.FinesRoute
import com.upsjb.movilsantarosa.feature.home.ui.HomeScreen
import com.upsjb.movilsantarosa.feature.auth.domain.model.UserRole
import com.upsjb.movilsantarosa.feature.members.ui.MembersScreen
import com.upsjb.movilsantarosa.feature.payments.PaymentsRoute

@Composable
fun MainNavHost(
    userRole: UserRole,
    onLogout: () -> Unit
) {

    val navigationState = rememberNavigationState(
        startRoute = HomeDestination,
        topLevelRoutes = MAIN_ROUTES
    )

    val navigator = remember {
        Navigator(navigationState)
    }

    val entryProvider = entryProvider {
        entry<HomeDestination> {
            HomeScreen()
        }

        entry<MembersDestination> {
            MembersScreen(
                onMemberClick = {}
            )
        }

        entry<FinesDestination> {
            FinesRoute()
        }

        entry<PaymentsDestination> {
            PaymentsRoute()
        }

        entry<AnnoucementsDestination> {
            AnnouncementsRoute()
        }
    }

    Scaffold(
        modifier = Modifier.safeDrawingPadding(),
        topBar = {
            AppTopBar(
                currentDestination = navigationState.topLevelRoute,
                onLogout = onLogout
            )
        },
        bottomBar = {
            AppBottomBar(
                currentDestination = navigationState.topLevelRoute,
                onDestinationSelected = navigator::navigate
            )
        },
        floatingActionButton = {
            AppFloatingActionButton(
                currentDestination = navigationState.topLevelRoute,
                userRole = userRole,
                onClick = {
                    when (navigationState.topLevelRoute) {
                        FinesDestination -> {
                        }

                        PaymentsDestination -> {
                        }

                        AnnoucementsDestination -> {
                        }

                        else -> Unit
                    }
                }
            )
        }
    ) { padding ->
        NavDisplay(
            entries = navigationState.toDecoratedEntries(entryProvider),
            onBack = {
                navigator.goBack()
            },
            modifier = Modifier
                .padding(padding)
                .background(MaterialTheme.colorScheme.surface)
        )
    }
}