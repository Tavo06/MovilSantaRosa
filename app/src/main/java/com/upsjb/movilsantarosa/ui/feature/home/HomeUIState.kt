package com.upsjb.movilsantarosa.ui.feature.home

import com.upsjb.movilsantarosa.domain.authentic.model.User

sealed class HomeUIState{
    data object Loading: HomeUIState()
    data class Success(val user: User) : HomeUIState()
    data class Error(val message: String) : HomeUIState()
}