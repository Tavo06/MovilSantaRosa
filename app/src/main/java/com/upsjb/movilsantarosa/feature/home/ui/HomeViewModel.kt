package com.upsjb.movilsantarosa.feature.home.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upsjb.movilsantarosa.feature.auth.domain.usecase.CurrentUserUseCase
import com.upsjb.movilsantarosa.feature.home.domain.usecase.GetHomeStatsUseCase
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
    private val getHomeStatsUseCase: GetHomeStatsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        getUserInfo()
        loadSummaryStats()
    }

    fun getUserInfo() {

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
                .onFailure { failure ->
                    _uiState.update {
                        it.copy(
                            userUiState = UserUiState.Error(
                                failure.message ?: "Usuario no logueado"
                            )
                        )
                    }
                }
        }
    }

    fun loadSummaryStats() {
        _uiState.update {
            it.copy(statsUiState = StatsUiState.Loading)
        }
        viewModelScope.launch {
            getHomeStatsUseCase().onSuccess { data ->
                _uiState.update {
                    it.copy(
                        statsUiState = StatsUiState.Success(
                            activeMembers = data.totalMembers,
                            totalFines = data.totalFines,
                            totalPayments = data.totalPayments,
                            totalAnnouncements = data.totalAnnouncements
                        )
                    )
                }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        statsUiState = StatsUiState.Error(message = error.message.orEmpty())
                    )
                }
            }

        }
    }
}