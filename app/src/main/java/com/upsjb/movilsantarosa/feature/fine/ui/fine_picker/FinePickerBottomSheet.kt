package com.upsjb.movilsantarosa.feature.fine.ui.fine_picker

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.upsjb.movilsantarosa.core.uicomponents.EmptySection
import com.upsjb.movilsantarosa.core.uicomponents.ErrorSection
import com.upsjb.movilsantarosa.core.uicomponents.FormTextField
import com.upsjb.movilsantarosa.core.uicomponents.SkeletonSection
import com.upsjb.movilsantarosa.core.utils.toCurrencyString
import com.upsjb.movilsantarosa.feature.fine.data.model.FineReason
import com.upsjb.movilsantarosa.feature.fine.data.model.FineStatus
import com.upsjb.movilsantarosa.feature.fine.domain.model.Fine

@Composable
fun FinePickerBottomSheet(
    memberEmail: String,
    onFineSelected: (Fine) -> Unit,
    onDismiss: () -> Unit,
    viewModel: FinePickerViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(memberEmail) {
        viewModel.loadFines(memberEmail)
    }

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
                text = "Seleccionar multa",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            IconButton(
                onClick = onDismiss
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = null
                )
            }
        }

        Spacer(Modifier.height(12.dp))

        when (val state = uiState) {

            FinePickerUiState.Loading -> {
                SkeletonSection(repeat = 5)
            }

            is FinePickerUiState.Error -> {
                ErrorSection(
                    title = state.message,
                    onRetry = {
                        viewModel.loadFines(memberEmail)
                    }
                )
            }

            is FinePickerUiState.Success -> {

                FormTextField(
                    value = state.query,
                    onValueChange = viewModel::updateQuery,
                    label = "Buscar multa",
                    placeholder = "Motivo, descripción o DNI",
                    singleLine = true
                )

                Spacer(Modifier.height(12.dp))

                if (state.filteredFines.isEmpty()) {

                    EmptySection(
                        title = "No se encontraron multas",
                        subtitle = "Intente con otro criterio de búsqueda."
                    )

                } else {

                    LazyColumn {
                        items(
                            items = state.filteredFines,
                            key = { it.id }
                        ) { fine ->

                            FinePickerItem(
                                fine = fine,
                                onClick = {
                                    onFineSelected(fine)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun FinePickerBottomSheetPreview() {

    val state = FinePickerUiState.Success(
        fines = listOf(
            Fine(
                id = "1",
                memberEmail = "juan@email.com",
                memberName = "Juan Pérez",
                memberDniNumber = "72751565",
                reason = FineReason.LATE_PAYMENT,
                amount = 50.0,
                description = "Pago fuera de fecha",
                issuedAt = "05/07/2026",
                dueDate = "15/07/2026",
                status = FineStatus.PENDING
            ),
            Fine(
                id = "2",
                memberEmail = "juan@email.com",
                memberName = "Juan Pérez",
                memberDniNumber = "72751565",
                reason = FineReason.ABSENCE_MEETING,
                amount = 30.0,
                description = "Inasistencia a reunión mensual",
                issuedAt = "01/07/2026",
                dueDate = "10/07/2026",
                status = FineStatus.PAID
            )
        )
    )

    MaterialTheme {

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
                    text = "Seleccionar multa",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                IconButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = null
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            FormTextField(
                value = state.query,
                onValueChange = {},
                label = "Buscar multa",
                placeholder = "Motivo, descripción o DNI",
                singleLine = true
            )

            Spacer(Modifier.height(12.dp))

            LazyColumn {
                items(state.filteredFines) { fine ->
                    FinePickerItem(
                        fine = fine,
                        onClick = {}
                    )
                }
            }
        }
    }
}

@Composable
fun FinePickerItem(
    fine: Fine,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                text = fine.reason.displayName,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            if (fine.reason == FineReason.OTHER && fine.customReason.isNotBlank()) {
                Spacer(Modifier.height(4.dp))

                Text(
                    text = fine.customReason,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = fine.issuedAt,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Text(
                    text = fine.amount.toCurrencyString(),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}