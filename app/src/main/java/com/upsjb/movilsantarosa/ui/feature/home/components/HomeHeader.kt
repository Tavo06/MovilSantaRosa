package com.upsjb.movilsantarosa.ui.feature.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.upsjb.movilsantarosa.ui.common.components.MessageDialog

@Composable
fun HomeHeader(
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showLogoutDialog by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
            .padding(16.dp)
    ) {

        IconButton(
            onClick = {
                showLogoutDialog = true
            },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(30.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.onPrimary)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Logout,
                contentDescription = "Cerrar sesión",
                tint = MaterialTheme.colorScheme.primary
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "'Honradez, Seguridad y Confianza'",
                fontStyle = FontStyle.Italic,
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "ASOCIACIÓN DE MOTOTAXIS",
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Santa Rosa de Lima",
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )

            Text(
                text = "San Clemente - Pisco",
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 12.sp,
                textAlign = TextAlign.Center
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
            cancelButtonText = "Cancelar"
        )
    }
}

@Preview(showBackground = true, name = "Home Header")
@Composable
fun PreviewHomeHeader() {
    HomeHeader(onLogout = {
        // Cerrar sesión
    })
}