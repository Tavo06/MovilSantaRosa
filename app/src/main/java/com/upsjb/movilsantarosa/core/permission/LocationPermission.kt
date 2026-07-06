package com.upsjb.movilsantarosa.core.permission

import android.Manifest
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LocationPermission(
    enabled: Boolean,
    onGranted: () -> Unit,
    onDenied: (() -> Unit)? = null
) {

    val permissionState = rememberPermissionState(
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    LaunchedEffect(enabled) {

        if (!enabled) return@LaunchedEffect

        if (permissionState.status.isGranted) {
            onGranted()
        } else {
            permissionState.launchPermissionRequest()
        }
    }

    LaunchedEffect(permissionState.status, enabled) {

        if (!enabled) return@LaunchedEffect

        if (permissionState.status.isGranted) {
            onGranted()
        } else {
            onDenied?.invoke()
        }
    }
}