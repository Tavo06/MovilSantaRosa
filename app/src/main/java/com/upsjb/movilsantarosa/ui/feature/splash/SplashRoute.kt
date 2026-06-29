package com.upsjb.movilsantarosa.ui.feature.splash

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun SplashRoute(
    modifier: Modifier = Modifier,
    onLoggedIn: () -> Unit,
    onLoggedOut: () -> Unit,
    splashViewModel: SplashViewModel = hiltViewModel()
) {

    val destination by splashViewModel.destination.collectAsStateWithLifecycle()

    LaunchedEffect(destination) {

        when (destination) {

            SplashDestination.Home ->
                onLoggedIn()

            SplashDestination.Login ->
                onLoggedOut()

            null -> Unit

        }

    }

    SplashScreen(modifier)

}