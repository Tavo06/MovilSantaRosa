package com.upsjb.movilsantarosa.feature.home.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upsjb.movilsantarosa.feature.auth.domain.usecase.CurrentUserUseCase
import com.upsjb.movilsantarosa.feature.home.domain.usecase.GetHomeStatsUseCase
import com.upsjb.movilsantarosa.feature.home.domain.usecase.GetMemberFinancialStatusUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val currentUserUseCase: CurrentUserUseCase,
    private val getHomeStatsUseCase: GetHomeStatsUseCase,
    private val getMemberFinancialStatusUseCase: GetMemberFinancialStatusUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        observeUser()
        observeStats()
        observeFinancialStatus()
    }

    fun observeUser() {
        _uiState.update {
            it.copy(userUiState = UserUiState.Loading)
        }

        viewModelScope.launch {
            currentUserUseCase()
                .onSuccess { user ->
                    _uiState.update {
                        it.copy(userUiState = UserUiState.Success(user))
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            userUiState = UserUiState.Error(
                                error.message ?: "Usuario no logueado"
                            )
                        )
                    }
                }
        }
    }

    private fun observeStats() {

        viewModelScope.launch {

            getHomeStatsUseCase()
                .collect { data ->

                    _uiState.update {
                        it.copy(
                            statsUiState = StatsUiState.Success(
                                activeMembers = data.totalMembers,
                                totalFines = data.totalFines,
                                totalPayments = data.totalPayments,
                                totalPost = data.totalPost,
                                pendingRegistrations = data.pendingRegistrations,
                                totalPostCount = data.totalPostCount,
                                latestPostTitle = data.latestPostTitle
                            )
                        )
                    }
                }
        }
    }

    private fun observeFinancialStatus() {
        viewModelScope.launch {
            getMemberFinancialStatusUseCase()
                .collect { status ->
                    _uiState.update {
                        it.copy(financialStatus = status)
                    }
                }
        }
    }
}