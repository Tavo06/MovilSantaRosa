package com.upsjb.movilsantarosa.feature.fine.ui.fine_picker

import com.upsjb.movilsantarosa.feature.fine.domain.model.Fine

sealed interface FinePickerUiState {

    data object Loading : FinePickerUiState

    data class Success(
        val query: String = "",
        val fines: List<Fine> = emptyList()
    ) : FinePickerUiState {

        val filteredFines: List<Fine>
            get() = if (query.isBlank()) {
                fines
            } else {
                fines.filter {
                    it.memberName.contains(query, ignoreCase = true) ||
                            it.memberDniNumber.contains(query) ||
                            it.reason.displayName.contains(query, ignoreCase = true) ||
                            it.description.contains(query, ignoreCase = true)
                }
            }
    }

    data class Error(
        val message: String
    ) : FinePickerUiState
}