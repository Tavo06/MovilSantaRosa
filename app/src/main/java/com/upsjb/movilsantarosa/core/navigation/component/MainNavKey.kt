package com.upsjb.movilsantarosa.core.navigation.component

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface MainNavKey : NavKey {

    @Serializable
    data object Splash : MainNavKey

    @Serializable
    data object Login : MainNavKey

    @Serializable
    data object Register : MainNavKey

    @Serializable
    data object Home : MainNavKey
}