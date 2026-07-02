package com.upsjb.movilsantarosa.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.upsjb.movilsantarosa.core.navigation.component.MainNavKey
import com.upsjb.movilsantarosa.ui.feature.login.LoginRoute
import com.upsjb.movilsantarosa.ui.feature.register.RegisterRoute
import com.upsjb.movilsantarosa.ui.feature.splash.SplashRoute

@Composable
fun AppNavHost() {

    val backStack = rememberNavBackStack(MainNavKey.Splash)

    fun navigate(key: MainNavKey) {
        backStack.add(key)
    }

    fun resetNavigation(key: MainNavKey) {
        backStack.clear()
        backStack.add(key)
    }
    NavDisplay(

        backStack = backStack,

        entryProvider = entryProvider {

            entry<MainNavKey.Splash> {

                SplashRoute(

                    onLoggedIn = {
                        resetNavigation(MainNavKey.Home)
                    }
                    ,

                    onLoggedOut = {
                        resetNavigation(MainNavKey.Login)
                    }

                )

            }

            entry<MainNavKey.Login> {

                LoginRoute(

                    onLoginSuccess = {
                        resetNavigation(MainNavKey.Home)
                    },

                    onRegisterClick = {

                        navigate(MainNavKey.Register)

                    }

                )

            }

            entry<MainNavKey.Register> {

                RegisterRoute(
                    onLoginNavigate = {
                        navigate(MainNavKey.Login)
                    },
                )

            }

            entry<MainNavKey.Home> {

                HomeNavHost(

                    onLogout = {
                        resetNavigation(MainNavKey.Splash)
                    }

                )

            }

        }

    )

}