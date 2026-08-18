package com.upsjb.movilsantarosa.core.uicomponents

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.upsjb.movilsantarosa.R
import com.upsjb.movilsantarosa.feature.auth.domain.model.UserStatus

@Composable
fun PendingApprovalScreen(
    status: UserStatus,
    onLogout: () -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showLogoutDialog by remember { mutableStateOf(false) }

    val isRejected = status == UserStatus.REJECTED

    val title = if (isRejected) "Registro rechazado" else "Registro pendiente"

    val message = if (isRejected) {
        "Tu solicitud de registro no fue aprobada por el administrador. No puedes acceder a la aplicación."
    } else {
        "Tu registro ha sido recibido y está a la espera de aprobación del administrador. " +
            "Podrás acceder a la aplicación una vez que un administrador apruebe tu registro."
    }

    val accentColor = if (isRejected) {
        MaterialTheme.colorScheme.error
    } else {
        MaterialTheme.colorScheme.primary
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .background(MaterialTheme.colorScheme.surface)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Image(
                painter = painterResource(R.drawable.logo_app),
                contentDescription = "Logo",
                modifier = Modifier.size(120.dp)
            )

            Icon(
                imageVector = if (isRejected) Icons.Default.Cancel else Icons.Default.HourglassTop,
                contentDescription = null,
                tint = accentColor,
                modifier = Modifier.size(48.dp)
            )

            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = accentColor,
                textAlign = TextAlign.Center
            )

            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            AppPrimaryButton(
                text = "Actualizar estado",
                onClick = onRefresh
            )

            AppOutlinedButton(
                text = "Cerrar sesión",
                onClick = { showLogoutDialog = true }
            )
        }
    }

    if (showLogoutDialog) {
        MessageDialog(
            title = "Cerrar sesión",
            message = "¿Desea realmente cerrar la sesión?",
            confirmButtonText = "Sí, cerrar",
            onConfirmClick = {
                showLogoutDialog = false
                onLogout()
            },
            onDismiss = {
                showLogoutDialog = false
            },
            cancelButtonText = "Cancelar"
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun PendingApprovalScreenInactivePreview() {
    PendingApprovalScreen(
        status = UserStatus.INACTIVE,
        onLogout = {},
        onRefresh = {}
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun PendingApprovalScreenRejectedPreview() {
    PendingApprovalScreen(
        status = UserStatus.REJECTED,
        onLogout = {},
        onRefresh = {}
    )
}
