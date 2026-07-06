package com.upsjb.movilsantarosa.feature.home.ui

import com.upsjb.movilsantarosa.feature.auth.domain.model.User

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
        val totalFines: Int,
        val totalPayments: Int,
        val totalAnnouncements: Int
    ) : StatsUiState()

    data class Error(val message: String) : StatsUiState()
}