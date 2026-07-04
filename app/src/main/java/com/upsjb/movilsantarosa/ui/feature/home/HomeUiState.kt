package com.upsjb.movilsantarosa.ui.feature.home

import com.upsjb.movilsantarosa.domain.authentic.model.User

data class HomeUiState(
    val userUiState: UserUiState = UserUiState.Loading,
    val statsUiState: StatsUiState = StatsUiState.Loading,
)

sealed class UserUiState {
    data object Loading : UserUiState()
    data class Success(val user: User) : UserUiState()
    data class Error(val message: String) : UserUiState()
}

sealed class StatsUiState {
    data object Loading : StatsUiState()
    data class Success(
        val activeMembers: Int,
        val debtors: Int,
        val paymentsOnTime: Int,
        val pendingFines: Int
    ) : StatsUiState()

    data class Error(val message: String) : StatsUiState()
}