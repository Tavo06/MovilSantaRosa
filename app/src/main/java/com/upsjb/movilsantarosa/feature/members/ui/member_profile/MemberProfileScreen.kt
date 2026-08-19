package com.upsjb.movilsantarosa.feature.members.ui.member_profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.upsjb.movilsantarosa.core.uicomponents.AppHeader
import com.upsjb.movilsantarosa.core.uicomponents.CardContent
import com.upsjb.movilsantarosa.core.uicomponents.ErrorSection
import com.upsjb.movilsantarosa.core.uicomponents.SkeletonSection
import com.upsjb.movilsantarosa.core.utils.toCurrencyString
import com.upsjb.movilsantarosa.core.utils.toDateString
import com.upsjb.movilsantarosa.feature.members.domain.model.Member
import com.upsjb.movilsantarosa.feature.members.ui.members.components.MemberStatusChip

@Composable
fun MemberProfileScreen(
    onBackClick: () -> Unit,
    viewModel: MemberProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {

        AppHeader(
            title = "Mi perfil",
            onBackClick = onBackClick
        )

        Spacer(modifier = Modifier.height(12.dp))

        when (val state = uiState) {

            MemberProfileUiState.Loading -> {
                SkeletonSection(modifier = Modifier.fillMaxWidth())
            }

            is MemberProfileUiState.Error -> {
                ErrorSection(
                    title = "Ups, tenemos incovenientes",
                    description = state.message
                )
            }

            is MemberProfileUiState.Success -> {
                MemberProfileContent(state = state)
            }
        }
    }
}

@Composable
private fun MemberProfileContent(state: MemberProfileUiState.Success) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        ProfileHeaderSection(member = state.member)
        FinancialSection(state = state)
        VehicleSection(member = state.member)
        DocumentationSection(member = state.member)
    }
}

@Composable
private fun ProfileHeaderSection(member: Member) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Surface(
            modifier = Modifier.size(72.dp),
            shape = CircleShape,
            color = MaterialTheme.colorScheme.primaryContainer
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = member.letterName,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = member.fullName,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(4.dp))

        MemberStatusChip(status = member.status)
    }
}

@Composable
private fun FinancialSection(state: MemberProfileUiState.Success) {
    val lastPayment = state.lastPayment
    val activeFines = state.activeFines

    SectionCard(title = "Información financiera") {

        if (lastPayment != null) {
            LabeledRow("Último pago", lastPayment.paidAt.toDateString())
            LabeledRow("Monto", lastPayment.amount.toCurrencyString())
        } else {
            Text(
                text = "Sin pagos registrados",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        LabeledRow("Multas activas", activeFines.size.toString())

        if (state.fines.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Historial de multas",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold
            )

            state.fines.forEach { fine ->
                LabeledRow(
                    label = fine.reason.displayName,
                    value = "${fine.amount.toCurrencyString()} • ${fine.status.displayName}"
                )
            }
        }
    }
}

@Composable
private fun VehicleSection(member: Member) {
    SectionCard(title = "Vehículo") {
        LabeledRow("Placa", member.plateNumber)
        LabeledRow("Color", member.vehicleColor)
    }
}

@Composable
private fun DocumentationSection(member: Member) {
    SectionCard(title = "Documentación") {
        LabeledRow("DNI", member.dniNumber)
        LabeledRow("Celular", member.phone)
        LabeledRow("N° de licencia", member.licenceNumber)
    }
}

@Composable
private fun SectionCard(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    CardContent {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(8.dp))

        content()
    }
}

@Composable
private fun LabeledRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value.ifBlank { "-" },
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium
        )
    }
}
