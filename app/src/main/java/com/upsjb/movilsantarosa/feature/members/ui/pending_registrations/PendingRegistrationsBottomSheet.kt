package com.upsjb.movilsantarosa.feature.members.ui.pending_registrations

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.upsjb.movilsantarosa.core.uicomponents.AppOutlinedButton
import com.upsjb.movilsantarosa.core.uicomponents.AppPrimaryButton
import com.upsjb.movilsantarosa.core.uicomponents.EmptySection
import com.upsjb.movilsantarosa.core.uicomponents.ErrorSection
import com.upsjb.movilsantarosa.core.uicomponents.MessageDialog
import com.upsjb.movilsantarosa.core.uicomponents.SkeletonSection
import com.upsjb.movilsantarosa.feature.members.domain.model.Member

@Composable
fun PendingRegistrationsBottomSheet(
    onDismiss: () -> Unit,
    viewModel: PendingRegistrationsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val errorMessage by viewModel.errorMessage.collectAsStateWithLifecycle()

    var memberToApprove by remember { mutableStateOf<Member?>(null) }
    var memberToReject by remember { mutableStateOf<Member?>(null) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = "Solicitudes de registro",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            IconButton(onClick = onDismiss) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = null
                )
            }
        }

        Spacer(Modifier.height(12.dp))

        when (val state = uiState) {

            PendingRegistrationsUiState.Loading -> {
                SkeletonSection(repeat = 3)
            }

            is PendingRegistrationsUiState.Error -> {
                ErrorSection(
                    title = state.message
                )
            }

            is PendingRegistrationsUiState.Success -> {

                if (state.members.isEmpty()) {

                    EmptySection(
                        title = "No hay solicitudes pendientes",
                        subtitle = "Las nuevas solicitudes de registro aparecerán aquí."
                    )

                } else {

                    LazyColumn {
                        items(
                            items = state.members,
                            key = { it.uid }
                        ) { member ->

                            PendingRegistrationItem(
                                member = member,
                                onApproveClick = { memberToApprove = member },
                                onRejectClick = { memberToReject = member }
                            )
                        }
                    }
                }
            }
        }
    }

    memberToApprove?.let { member ->
        MessageDialog(
            title = "¿Aprobar registro?",
            message = "${member.fullName} podrá acceder a la aplicación.",
            confirmButtonText = "Aprobar",
            cancelButtonText = "Cancelar",
            onConfirmClick = {
                viewModel.approve(member)
                memberToApprove = null
            },
            onDismiss = { memberToApprove = null }
        )
    }

    memberToReject?.let { member ->
        MessageDialog(
            title = "¿Rechazar registro?",
            message = "${member.fullName} no podrá acceder a la aplicación.",
            confirmButtonText = "Rechazar",
            cancelButtonText = "Cancelar",
            onConfirmClick = {
                viewModel.reject(member)
                memberToReject = null
            },
            onDismiss = { memberToReject = null }
        )
    }

    errorMessage?.let { message ->
        MessageDialog(
            title = "Aviso",
            message = message,
            confirmButtonText = "Aceptar",
            onConfirmClick = { viewModel.clearError() },
            onDismiss = { viewModel.clearError() }
        )
    }
}

@Composable
private fun PendingRegistrationItem(
    member: Member,
    onApproveClick: () -> Unit,
    onRejectClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                text = member.fullName,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = "DNI: ${member.dniNumber}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = member.email,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = "Celular: ${member.phone}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                AppOutlinedButton(
                    text = "Rechazar",
                    onClick = onRejectClick,
                    modifier = Modifier.weight(1f)
                )

                AppPrimaryButton(
                    text = "Aprobar",
                    onClick = onApproveClick,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}
