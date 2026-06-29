package com.upsjb.movilsantarosa.ui.feature.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upsjb.movilsantarosa.domain.authentic.usecase.CurrentUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val currentUserUseCase: CurrentUserUseCase
) : ViewModel() {

    private val _destination = MutableStateFlow<SplashDestination?>(null)
    val destination: StateFlow<SplashDestination?> = _destination

    init {
        validateSession()
    }

    private fun validateSession() {
        viewModelScope.launch {
            delay(1000L.milliseconds)
            val user = currentUserUseCase()

            _destination.value =
                if (user != null) {
                    SplashDestination.Home
                } else {
                    SplashDestination.Login
                }
        }
    }
}

sealed interface SplashDestination {
    data object Home : SplashDestination
    data object Login : SplashDestination
}