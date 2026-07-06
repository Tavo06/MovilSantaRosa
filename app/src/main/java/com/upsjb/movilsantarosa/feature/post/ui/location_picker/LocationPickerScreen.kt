package com.upsjb.movilsantarosa.feature.post.ui.location_picker

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.upsjb.movilsantarosa.core.uicomponents.AppHeaderTransparent
import com.upsjb.movilsantarosa.core.uicomponents.ErrorSection
import com.upsjb.movilsantarosa.core.uicomponents.ProgressIndicatorOverlay
import com.upsjb.movilsantarosa.feature.post.domain.model.Location
import com.upsjb.movilsantarosa.feature.post.ui.location_picker.components.MapControls
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.maplibre.compose.camera.CameraPosition
import org.maplibre.compose.camera.rememberCameraState
import org.maplibre.compose.map.GestureOptions
import org.maplibre.compose.map.MapOptions
import org.maplibre.compose.map.MaplibreMap
import org.maplibre.compose.map.OrnamentOptions
import org.maplibre.compose.style.BaseStyle
import org.maplibre.compose.style.rememberStyleState
import org.maplibre.spatialk.geojson.Position

@Composable
fun LocationPickerScreen(
    onLocationSelected: (Location) -> Unit,
    onBack: () -> Unit,
    viewModel: LocationPickerViewModel = hiltViewModel()
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()
    val selectedLocation by viewModel.selectedLocation.collectAsStateWithLifecycle()
    val loading by viewModel.isResolvingAddress.collectAsStateWithLifecycle()

    LaunchedEffect(selectedLocation) {
        selectedLocation?.let {
            onLocationSelected(it)
            onBack()
        }
    }

    if (loading) {
        ProgressIndicatorOverlay()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when (val map = uiState) {

            LocationPickerUiState.Loading -> {
                ProgressIndicatorOverlay()
            }

            is LocationPickerUiState.Error -> {
                ErrorSection(
                    title = map.message,
                    onRetry = viewModel::loadCurrentLocation
                )
            }

            is LocationPickerUiState.Ready -> {

                LocationPickerContent(
                    initialLocation = map.location,
                    confirmLocation = { location ->
                        viewModel.confirmLocation(location)
                    },
                    onBack = onBack
                )
            }
        }
    }
}

@Composable
private fun LocationPickerContent(
    initialLocation: Location,
    confirmLocation: (Location) -> Unit,
    onBack: () -> Unit
) {
    val cameraState = rememberCameraState(
        CameraPosition(
            target = Position(
                longitude = initialLocation.longitude,
                latitude = initialLocation.latitude
            ),
            zoom = 16.0
        )
    )

    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        MaplibreMap(
            baseStyle = BaseStyle.Uri(
                "https://tiles.openfreemap.org/styles/liberty"
            ),
            cameraState = cameraState,
            styleState = rememberStyleState(),
            options = MapOptions(
                ornamentOptions = OrnamentOptions.AllDisabled,
                gestureOptions = GestureOptions(
                    isScrollEnabled = true,
                    isZoomEnabled = true,
                    isRotateEnabled = true,
                    isTiltEnabled = false
                )
            )
        )

        Icon(
            imageVector = Icons.Default.LocationOn,
            contentDescription = null,
            tint = Color.Red,
            modifier = Modifier
                .align(Alignment.Center)
                .size(48.dp)
        )

        AppHeaderTransparent(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            title = "Seleccionar ubicación",
            onBackClick = onBack
        )

        MapControls(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp, bottom = 16.dp),

            onZoomIn = {
                scope.launch {
                    cameraState.animateTo(
                        cameraState.position.copy(
                            zoom = cameraState.position.zoom + 1
                        )
                    )
                }
            },

            onZoomOut = {
                scope.launch {
                    cameraState.animateTo(
                        cameraState.position.copy(
                            zoom = cameraState.position.zoom - 1
                        )
                    )
                }
            },
            onSelect = {
                confirmLocation(
                    initialLocation
                )
                scope.launch(Dispatchers.Default) {
                    val center = cameraState.position.target
                    confirmLocation(
                        Location(
                            latitude = center.latitude,
                            longitude = center.longitude
                        )
                    )
                }
            }
        )
    }
}