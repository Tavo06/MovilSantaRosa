package com.upsjb.movilsantarosa.feature.auth.ui.login

import com.upsjb.movilsantarosa.feature.auth.domain.model.User

sealed class LoginUIState{
    data object Idle: LoginUIState()
    data object Loading: LoginUIState()
    data class Success(val user: User) : LoginUIState()
    data class Error(val message: String) : LoginUIState()
}