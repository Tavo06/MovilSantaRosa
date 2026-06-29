package com.upsjb.movilsantarosa.core.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.upsjb.movilsantarosa.core.navigation.component.BottomNavKey
import com.upsjb.movilsantarosa.ui.feature.announcements.AnnouncementsRoute
import com.upsjb.movilsantarosa.ui.feature.fine.FinesRoute
import com.upsjb.movilsantarosa.ui.feature.home.HomeRoute
import com.upsjb.movilsantarosa.ui.feature.members.MembersRoute

@Composable
fun HomeNavHost(
    onLogout: () -> Unit
) {

    val homeStack =
        rememberNavBackStack(BottomNavKey.HomeDestination)

    val membersStack =
        rememberNavBackStack(BottomNavKey.MembersDestination)

    val finesStack =
        rememberNavBackStack(BottomNavKey.FinesDestination)

    val announcementsStack =
        rememberNavBackStack(BottomNavKey.AnnouncementsDestination)

    var currentTab by rememberSaveable(
        stateSaver = BottomNavKey.stateSaver
    ) {
        mutableStateOf(BottomNavKey.HomeDestination)
    }

    val stacks = mapOf(
        BottomNavKey.HomeDestination to homeStack,
        BottomNavKey.MembersDestination to membersStack,
        BottomNavKey.FinesDestination to finesStack,
        BottomNavKey.AnnouncementsDestination to announcementsStack
    )

    val currentStack = stacks.getValue(currentTab)

    fun navigate(key: NavKey) {
        currentStack.add(key)
    }

    fun pop() {
        currentStack.removeLastOrNull()
    }

    fun <T : NavKey> reset(stack: NavBackStack<T>) {
        while (stack.size > 1) {
            stack.removeLastOrNull()
        }
    }

    Scaffold(

        bottomBar = {

            NavigationBar {

                BottomNavKey.items.forEach { tab ->

                    NavigationBarItem(

                        selected = tab == currentTab,

                        onClick = {

                            if (tab == currentTab) {

                                reset(stacks.getValue(tab))

                            } else {

                                currentTab = tab

                            }

                        },

                        icon = {
                            Icon(tab.icon, null)
                        },

                        label = {
                            Text(tab.label)
                        }

                    )

                }

            }

        }

    ) { padding ->

        NavDisplay(

            modifier = Modifier.padding(padding),

            backStack = currentStack,

            entryProvider = entryProvider {

                entry<BottomNavKey.HomeDestination> {

                    HomeRoute(

                        onLogout = onLogout

                    )

                }

                entry<BottomNavKey.MembersDestination> {

                    MembersRoute(

                        onOpenMember = {


                        }

                    )

                }


                entry<BottomNavKey.FinesDestination> {

                    FinesRoute()

                }

                entry<BottomNavKey.AnnouncementsDestination> {

                    AnnouncementsRoute()

                }

            }

        )

    }

}