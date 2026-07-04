package com.upsjb.movilsantarosa.ui.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upsjb.movilsantarosa.domain.authentic.usecase.CurrentUserUseCase
import com.upsjb.movilsantarosa.domain.home.usecase.GetUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val currentUserUseCase: CurrentUserUseCase,
    private val getUserUseCase: GetUserUseCase,
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
            val user = currentUserUseCase()
            if (user != null) {
                delay(2_000.milliseconds)
                getUserUseCase(user.uid).onSuccess { data ->
                    _uiState.update {
                        it.copy(userUiState = UserUiState.Success(data))
                    }
                }.onFailure {
                    _uiState.update {
                        it.copy(userUiState = UserUiState.Error("Usuario no encontrado"))
                    }
                }
            } else {
                _uiState.update {
                    it.copy(userUiState = UserUiState.Error("Usuario no logueado"))
                }
            }
        }
    }

    fun loadSummaryStats() {
        _uiState.update {
            it.copy(statsUiState = StatsUiState.Loading)
        }
        viewModelScope.launch {

            _uiState.update {
                it.copy(statsUiState = StatsUiState.Loading)
            }

            delay(2_000.milliseconds)

            _uiState.update {
                it.copy(
                    statsUiState = StatsUiState.Success(
                        activeMembers = 48,
                        debtors = 12,
                        paymentsOnTime = 36,
                        pendingFines = 7
                    )
                )
            }
        }
    }
}