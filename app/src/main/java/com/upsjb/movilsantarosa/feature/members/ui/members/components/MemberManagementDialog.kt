package com.upsjb.movilsantarosa.feature.members.ui.members.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.upsjb.movilsantarosa.core.uicomponents.AppOutlinedButton
import com.upsjb.movilsantarosa.core.uicomponents.AppPrimaryButton
import com.upsjb.movilsantarosa.core.uicomponents.FormTextField
import com.upsjb.movilsantarosa.core.uicomponents.MessageDialog
import com.upsjb.movilsantarosa.feature.auth.domain.model.UserStatus
import com.upsjb.movilsantarosa.feature.members.domain.model.Member
import com.upsjb.movilsantarosa.feature.members.ui.members.MemberActionState

@Composable
fun MemberManagementDialog(
    member: Member,
    actionState: MemberActionState,
    onSave: (Member) -> Unit,
    onToggleStatus: () -> Unit,
    onDismiss: () -> Unit,
    onResetAction: () -> Unit
) {
    var firstname by remember(member) { mutableStateOf(member.firstname) }
    var lastname by remember(member) { mutableStateOf(member.lastname) }
    var dniNumber by remember(member) { mutableStateOf(member.dniNumber) }
    var birthdate by remember(member) { mutableStateOf(member.birthdate) }
    var phone by remember(member) { mutableStateOf(member.phone) }
    var plateNumber by remember(member) { mutableStateOf(member.plateNumber) }
    var licenceNumber by remember(member) { mutableStateOf(member.licenceNumber) }
    var vehicleColor by remember(member) { mutableStateOf(member.vehicleColor) }

    var showStatusConfirm by remember { mutableStateOf(false) }

    val isDeactivating = member.status == UserStatus.ACTIVE
    val isLoading = actionState is MemberActionState.Loading

    LaunchedEffect(actionState) {
        if (actionState is MemberActionState.Success) {
            onDismiss()
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = "Gestionar socio")
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                FormTextField(
                    value = firstname,
                    onValueChange = { firstname = it },
                    label = "Nombres"
                )

                FormTextField(
                    value = lastname,
                    onValueChange = { lastname = it },
                    label = "Apellidos"
                )

                FormTextField(
                    value = dniNumber,
                    onValueChange = { dniNumber = it },
                    label = "DNI"
                )

                FormTextField(
                    value = birthdate,
                    onValueChange = { birthdate = it },
                    label = "Fecha de nacimiento"
                )

                FormTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = "Celular"
                )

                FormTextField(
                    value = plateNumber,
                    onValueChange = { plateNumber = it },
                    label = "Placa"
                )

                FormTextField(
                    value = licenceNumber,
                    onValueChange = { licenceNumber = it },
                    label = "N° de licencia"
                )

                FormTextField(
                    value = vehicleColor,
                    onValueChange = { vehicleColor = it },
                    label = "Color del vehículo"
                )

                AppPrimaryButton(
                    text = "Guardar cambios",
                    isLoading = isLoading,
                    onClick = {
                        onSave(
                            member.copy(
                                firstname = firstname,
                                lastname = lastname,
                                dniNumber = dniNumber,
                                birthdate = birthdate,
                                phone = phone,
                                plateNumber = plateNumber,
                                licenceNumber = licenceNumber,
                                vehicleColor = vehicleColor
                            )
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                AppOutlinedButton(
                    text = if (isDeactivating) "Desactivar socio" else "Reactivar socio",
                    isLoading = isLoading,
                    onClick = { showStatusConfirm = true },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cancelar")
            }
        }
    )

    if (actionState is MemberActionState.Error) {
        MessageDialog(
            title = "Aviso",
            message = actionState.message,
            confirmButtonText = "Aceptar",
            onConfirmClick = onResetAction,
            onDismiss = onResetAction
        )
    }

    if (showStatusConfirm) {
        MessageDialog(
            title = if (isDeactivating) "¿Desactivar socio?" else "¿Reactivar socio?",
            message = if (isDeactivating) {
                "${member.fullName} ya no podrá acceder a la aplicación."
            } else {
                "${member.fullName} podrá acceder nuevamente a la aplicación."
            },
            confirmButtonText = if (isDeactivating) "Desactivar" else "Reactivar",
            cancelButtonText = "Cancelar",
            onConfirmClick = {
                showStatusConfirm = false
                onToggleStatus()
            },
            onDismiss = { showStatusConfirm = false }
        )
    }
}
