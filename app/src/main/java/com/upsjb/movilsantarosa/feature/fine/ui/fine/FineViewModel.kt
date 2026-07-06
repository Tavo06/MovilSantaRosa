package com.upsjb.movilsantarosa.feature.fine.ui.fine

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upsjb.movilsantarosa.feature.fine.domain.usecase.GetFinesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.collections.filter

@HiltViewModel
class FineViewModel @Inject constructor(
    getFinesUseCase: GetFinesUseCase,
) : ViewModel() {

    private val query = MutableStateFlow("")

    private val finesFlow = getFinesUseCase()

    val uiState = combine(
        finesFlow,
        query
    ) { fines, query ->

        val filtered = if (query.isBlank()) {
            fines
        } else {
            fines.filter { fine ->
                fine.memberName.contains(query, true) ||
                        fine.memberEmail.contains(query, true) ||
                        fine.reason.name.contains(query, true) ||
                        fine.customReason.contains(query, true)
            }
        }

        FineUiState.Success(
            fines = filtered,
            query = query
        )
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = FineUiState.Loading
        )

    fun updateQuery(query: String) {
        this.query.value = query
    }
}