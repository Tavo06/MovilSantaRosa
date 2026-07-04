package com.upsjb.movilsantarosa.core.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.upsjb.movilsantarosa.core.navigation.component.AnnoucementsDestination
import com.upsjb.movilsantarosa.core.navigation.component.BOTTOM_BAR_ITEMS
import com.upsjb.movilsantarosa.core.navigation.component.FinesDestination
import com.upsjb.movilsantarosa.core.navigation.component.HomeDestination
import com.upsjb.movilsantarosa.core.navigation.component.MAIN_ROUTES
import com.upsjb.movilsantarosa.core.navigation.component.MembersDestination
import com.upsjb.movilsantarosa.core.navigation.component.Navigator
import com.upsjb.movilsantarosa.core.navigation.component.PaymentsDestination
import com.upsjb.movilsantarosa.core.navigation.component.rememberNavigationState
import com.upsjb.movilsantarosa.ui.feature.announcements.AnnouncementsRoute
import com.upsjb.movilsantarosa.ui.feature.fine.FinesRoute
import com.upsjb.movilsantarosa.ui.feature.home.HomeRoute
import com.upsjb.movilsantarosa.ui.feature.members.MembersRoute
import com.upsjb.movilsantarosa.ui.feature.payments.PaymentsRoute

@Composable
fun MainNavHost(
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
            HomeRoute(
                onLogout = onLogout
            )
        }

        entry<MembersDestination> {
            MembersRoute(
                onOpenMember = {

                }
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
        bottomBar = {
            NavigationBar {
                BOTTOM_BAR_ITEMS.forEach { (key, item) ->
                    NavigationBarItem(
                        selected = navigationState.topLevelRoute == key,
                        onClick = {
                            navigator.navigate(key)
                        },
                        icon = {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.description
                            )
                        },
                        label = {
                            Text(item.description)
                        }
                    )
                }
            }
        }

    ) { padding ->
        NavDisplay(
            entries = navigationState.toDecoratedEntries(entryProvider),
            onBack = {
                navigator.goBack()
            },
            modifier = Modifier.padding(padding)
        )
    }
}