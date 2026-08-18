package com.upsjb.movilsantarosa.feature.post.ui.location_picker.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CenterFocusWeak
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MapControls(
    modifier: Modifier = Modifier,
    onZoomIn: () -> Unit,
    onZoomOut: () -> Unit,
    onSelect: () -> Unit,
    showSelect: Boolean = true,
) {

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.End
    ) {

        if (showSelect) {
            FloatingActionButton(
                onClick = onSelect
            ) {
                Icon(
                    imageVector = Icons.Default.CenterFocusWeak,
                    contentDescription = "onSelect"
                )
            }
        }

        FloatingActionButton(
            onClick = onZoomIn
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Acercar"
            )
        }

        FloatingActionButton(
            onClick = onZoomOut
        ) {
            Icon(
                imageVector = Icons.Default.Remove,
                contentDescription = "Alejar"
            )
        }
    }
}