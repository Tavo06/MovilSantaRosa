package com.upsjb.movilsantarosa.feature.fine.ui.fine_picker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upsjb.movilsantarosa.feature.fine.data.model.FineStatus
import com.upsjb.movilsantarosa.feature.fine.domain.model.Fine
import com.upsjb.movilsantarosa.feature.fine.domain.usecase.GetFinesByEmailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FinePickerViewModel @Inject constructor(
    private val getFinesByEmailUseCase: GetFinesByEmailUseCase
) : ViewModel() {

    private val query = MutableStateFlow("")

    private val finesFlow = MutableStateFlow<List<Fine>>(emptyList())

    fun loadFines(memberEmail: String) {

        viewModelScope.launch {

            getFinesByEmailUseCase(memberEmail)
                .collect { fines ->
                    finesFlow.value = fines.filter { it.status == FineStatus.PENDING }
                }
        }
    }

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

        FinePickerUiState.Success(
            fines = filtered,
            query = query
        )
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = FinePickerUiState.Loading
        )

    fun updateQuery(query: String) {
        this.query.value = query
    }
}