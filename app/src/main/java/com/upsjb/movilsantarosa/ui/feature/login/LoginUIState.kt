package com.upsjb.movilsantarosa.ui.feature.login

import com.upsjb.movilsantarosa.domain.authentic.model.User

sealed class LoginUIState{
    data object Idle: LoginUIState()
    data object Loading: LoginUIState()

    data class Success(val user: User) : LoginUIState()
    data class Error(val message: String) : LoginUIState()
}