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
import com.upsjb.movilsantarosa.core.navigation.component.FineFormDestination
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
import com.upsjb.movilsantarosa.feature.announcements.AnnouncementsScreen
import com.upsjb.movilsantarosa.feature.home.ui.HomeScreen
import com.upsjb.movilsantarosa.feature.auth.domain.model.UserRole
import com.upsjb.movilsantarosa.feature.fine.ui.fine.FinesScreen
import com.upsjb.movilsantarosa.feature.fine.ui.fine_form.FineFormScreen
import com.upsjb.movilsantarosa.feature.members.ui.MembersScreen
import com.upsjb.movilsantarosa.feature.payments.PaymentsScreen

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
            FinesScreen(
                onFineClick = {}
            )
        }
        entry<FineFormDestination> {
            FineFormScreen(
                onBackClick = {
                    navigator.goBack()
                }
            )
        }

        entry<PaymentsDestination> {
            PaymentsScreen()
        }

        entry<AnnoucementsDestination> {
            AnnouncementsScreen()
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
                currentDestination = navigationState.currentDestination,
                userRole = userRole,
                onClick = {
                    when (navigationState.topLevelRoute) {
                        FinesDestination -> {
                            navigator.navigate(FineFormDestination)
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