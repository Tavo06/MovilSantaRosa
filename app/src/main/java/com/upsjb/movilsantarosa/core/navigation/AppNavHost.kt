package com.upsjb.movilsantarosa.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.upsjb.movilsantarosa.MainViewModel
import com.upsjb.movilsantarosa.SessionState
import com.upsjb.movilsantarosa.core.uicomponents.SplashScreen

@Composable
fun AppNavHost(
    mainViewModel: MainViewModel = hiltViewModel()
) {
    val session by mainViewModel.session.collectAsStateWithLifecycle()
    when (val uiState = session) {
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

        is SessionState.LoggedIn -> {
            MainNavHost(
                userRole = uiState.role,
                onLogout = {
                    mainViewModel.logout()
                }
            )
        }
    }
}