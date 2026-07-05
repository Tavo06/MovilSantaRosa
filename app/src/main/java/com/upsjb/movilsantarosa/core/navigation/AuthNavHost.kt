package com.upsjb.movilsantarosa.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.upsjb.movilsantarosa.core.navigation.component.AUTH_ROUTES
import com.upsjb.movilsantarosa.core.navigation.component.LoginDestination
import com.upsjb.movilsantarosa.core.navigation.component.Navigator
import com.upsjb.movilsantarosa.core.navigation.component.RegisterDestination
import com.upsjb.movilsantarosa.core.navigation.component.rememberNavigationState
import com.upsjb.movilsantarosa.feature.auth.ui.login.LoginScreen
import com.upsjb.movilsantarosa.feature.auth.ui.register.RegisterScreen

@Composable
fun AuthNavHost(
    onLoginSuccess: () -> Unit
) {

    val navigationState = rememberNavigationState(
        startRoute = LoginDestination,
        topLevelRoutes = AUTH_ROUTES
    )

    val navigator = remember {
        Navigator(navigationState)
    }

    val entryProvider = entryProvider {

        entry<LoginDestination> {

            LoginScreen(
                onLoginSuccess = onLoginSuccess,
                onRegisterClick = {
                    navigator.navigate(RegisterDestination)
                }
            )
        }

        entry<RegisterDestination> {

            RegisterScreen(
                navigateToLogin = {
                    navigator.navigate(LoginDestination)
                }
            )
        }
    }

    NavDisplay(
        entries = navigationState.toDecoratedEntries(entryProvider),
        onBack = {
            navigator.goBack()
        }
    )
}