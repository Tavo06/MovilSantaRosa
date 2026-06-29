package com.upsjb.movilsantarosa.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.upsjb.movilsantarosa.ui.feature.login.LoginRoute

@Composable
fun AppNavHost() {

    val backStack = rememberNavBackStack(LoginDestination)

    NavDisplay(

        backStack = backStack,

        entryProvider = entryProvider {

            entry<LoginDestination> {

                LoginRoute(

                    onLoginSuccess = {
                        backStack.clear()
                        backStack.add(HomeDestination)
                    },
                    onRegisterClick = {

                    }

                )

            }

            entry<HomeDestination> {

                HomeRoute()

            }

        }

    )

}

@Composable
fun HomeRoute() {
    Text("HOME")
}