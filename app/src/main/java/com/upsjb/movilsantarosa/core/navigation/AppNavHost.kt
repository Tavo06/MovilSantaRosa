package com.upsjb.movilsantarosa.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.upsjb.movilsantarosa.MainViewModel
import com.upsjb.movilsantarosa.SessionState
import com.upsjb.movilsantarosa.ui.feature.splash.SplashScreen

@Composable
fun AppNavHost(
    mainViewModel: MainViewModel = hiltViewModel()
) {
    val session by mainViewModel.session.collectAsStateWithLifecycle()
    when (session) {
        SessionState.Loading -> {
            SplashScreen()
        }

        SessionState.LoggedOut -> {
            AuthNavHost(
                onLoginSuccess = {
                    mainViewModel.refreshSession()
                }
            )
        }

        SessionState.LoggedIn -> {
            MainNavHost(
                onLogout = {
                    mainViewModel.logout()
                }
            )
        }
    }
}