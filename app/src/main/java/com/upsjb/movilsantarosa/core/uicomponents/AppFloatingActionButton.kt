package com.upsjb.movilsantarosa.core.uicomponents

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation3.runtime.NavKey
import com.upsjb.movilsantarosa.core.navigation.component.AnnoucementsDestination
import com.upsjb.movilsantarosa.core.navigation.component.FinesDestination
import com.upsjb.movilsantarosa.core.navigation.component.PaymentsDestination
import com.upsjb.movilsantarosa.feature.auth.domain.model.UserRole

@Composable
fun AppFloatingActionButton(
    currentDestination: NavKey?,
    userRole: UserRole,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    if (userRole != UserRole.ADMIN) return

    val destination = currentDestination ?: return

    val item = FAB_ITEMS[destination] ?: return

    FloatingActionButton(
        modifier = modifier,
        onClick = onClick,
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary
    ) {
        Icon(
            imageVector = item.icon,
            contentDescription = item.contentDescription
        )
    }
}

data class FabItem(
    val icon: ImageVector,
    val contentDescription: String
)

val FAB_ITEMS = mapOf(
    FinesDestination to FabItem(
        icon = Icons.Default.Add,
        contentDescription = "Registrar multa"
    ),
    PaymentsDestination to FabItem(
        icon = Icons.Default.Add,
        contentDescription = "Registrar pago"
    ),
    AnnoucementsDestination to FabItem(
        icon = Icons.Default.Add,
        contentDescription = "Crear anuncio"
    )
)