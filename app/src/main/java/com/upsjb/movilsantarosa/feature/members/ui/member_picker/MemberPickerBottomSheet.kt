package com.upsjb.movilsantarosa.feature.members.ui.member_picker

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.upsjb.movilsantarosa.feature.members.domain.model.Member

@Composable
fun MemberPickerBottomSheet(
    onMemberSelected: (Member) -> Unit,
    onDismiss: () -> Unit,
    viewModel: MemberPickerViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

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
                text = "Seleccionar socio",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            IconButton(
                onClick = onDismiss
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Cerrar"
                )
            }
        }

        Spacer(Modifier.height(12.dp))

        when (val state = uiState) {

            MemberPickerUiState.Loading -> {
                SkeletonSection(repeat = 5)
            }

            is MemberPickerUiState.Error -> {
                ErrorSection(
                    title = "Ups, tenemos incovenientes",
                    description = state.message
                )
            }

            is MemberPickerUiState.Success -> {

                FormTextField(
                    value = state.query,
                    onValueChange = viewModel::updateQuery,
                    label = "Buscar socio",
                    placeholder = "Nombre, correo o DNI",
                    singleLine = true
                )

                Spacer(Modifier.height(12.dp))

                if (state.filteredMembers.isEmpty()) {
                    EmptySection(
                        title = "No se encontraron socios",
                        subtitle = "Intente con otro criterio de búsqueda."
                    )
                } else {
                    LazyColumn {
                        items(
                            items = state.filteredMembers,
                            key = { it.email }
                        ) { member ->
                            MemberPickerItem(
                                member = member,
                                onClick = {
                                    onMemberSelected(member)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MemberPickerItem(
    member: Member,
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

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.size(44.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = member.letterName,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            Spacer(Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = member.fullName,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "DNI: ${member.dniNumber}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MemberPickerItemPreview() {

    val member = Member(
        firstname = "Juan",
        lastname = "Perez",
        email = "juan@email.com",
        dniNumber = "12345678"
    )

    MaterialTheme {
        MemberPickerItem(
            member = member,
            onClick = {}
        )
    }
}