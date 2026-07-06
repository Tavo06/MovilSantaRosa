package com.upsjb.movilsantarosa.feature.post.ui.location_picker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upsjb.movilsantarosa.feature.post.domain.model.Location
import com.upsjb.movilsantarosa.feature.post.domain.usecase.GetAddressUseCase
import com.upsjb.movilsantarosa.feature.post.domain.usecase.GetCurrentLocationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationPickerViewModel @Inject constructor(
    val getCurrentLocationUseCase: GetCurrentLocationUseCase,
    private val getAddressUseCase: GetAddressUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<LocationPickerUiState>(LocationPickerUiState.Loading)
    val state = _state.asStateFlow()

    private val _selectedLocation = MutableStateFlow<Location?>(null)
    val selectedLocation = _selectedLocation.asStateFlow()

    private val _isResolvingAddress = MutableStateFlow(false)
    val isResolvingAddress = _isResolvingAddress.asStateFlow()

    init {
        loadCurrentLocation()
    }

    fun loadCurrentLocation() {
        viewModelScope.launch {

            _state.update {
                LocationPickerUiState.Loading
            }

            getCurrentLocationUseCase()
                .onSuccess { location ->

                    _state.update {
                        LocationPickerUiState.Ready(location)
                    }
                }
                .onFailure { error ->
                    _state.update {
                        LocationPickerUiState.Error(
                            error.message ?: "No se pudo obtener la ubicación actual."
                        )
                    }
                }
        }
    }

    fun confirmLocation(location: Location) {
        _isResolvingAddress.value = true
        viewModelScope.launch {
            getAddressUseCase(
                latitude = location.latitude,
                longitude = location.longitude
            ).onSuccess {
                _isResolvingAddress.value = false
                _selectedLocation.value = location.copy(address = it)
            }.onFailure {
                _isResolvingAddress.value = false
            }
        }
    }
}