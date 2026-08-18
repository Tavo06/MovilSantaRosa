package com.upsjb.movilsantarosa.feature.members.ui.members.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.ManageAccounts
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.upsjb.movilsantarosa.core.uicomponents.AppOutlinedButton
import com.upsjb.movilsantarosa.core.uicomponents.AppPrimaryButton

@Composable
fun ContactMethodDialog(
    phoneNumber: String,
    isAdmin: Boolean = false,
    onWhatsAppClick: (String) -> Unit,
    onCallClick: (String) -> Unit,
    onManageClick: () -> Unit = {},
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = "Contactar usuario")
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {

                Text(
                    text = "Elige cómo deseas comunicarte con este número:",
                    style = MaterialTheme.typography.bodyMedium
                )

                Text(
                    text = phoneNumber,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {

                    AppPrimaryButton(
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Message,
                                contentDescription = null
                            )
                        },
                        text = "WhatsApp",
                        onClick = {
                            onWhatsAppClick(phoneNumber)
                            onDismiss()
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    AppOutlinedButton(
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Call,
                                contentDescription = null
                            )
                        },
                        text = "Llamar",
                        onClick = {
                            onCallClick(phoneNumber)
                            onDismiss()
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    if (isAdmin) {
                        AppOutlinedButton(
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.ManageAccounts,
                                    contentDescription = null
                                )
                            },
                            text = "Gestionar socio",
                            onClick = {
                                onManageClick()
                                onDismiss()
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
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
}