package com.upsjb.movilsantarosa.feature.post.ui.location_picker

import com.upsjb.movilsantarosa.feature.post.domain.model.Location

sealed interface LocationPickerUiState {

    data object Loading : LocationPickerUiState

    data class Ready(
        val location: Location
    ) : LocationPickerUiState

    data class Error(
        val message: String
    ) : LocationPickerUiState
}