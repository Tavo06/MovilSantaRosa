package com.upsjb.movilsantarosa.feature.fine.ui.fine

import com.upsjb.movilsantarosa.feature.fine.domain.model.Fine

sealed class FineUiState {

    data object Loading : FineUiState()

    data class Success(
        val query: String = "",
        val fines: List<Fine> = emptyList(),
    ) : FineUiState()

    data class Error(
        val message: String
    ) : FineUiState()
}