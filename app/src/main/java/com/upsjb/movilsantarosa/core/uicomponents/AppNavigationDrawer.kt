package com.upsjb.movilsantarosa.core.uicomponents

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import com.upsjb.movilsantarosa.R
import com.upsjb.movilsantarosa.core.navigation.component.BOTTOM_BAR_ITEMS

@Composable
fun AppNavigationDrawer(
    currentDestination: NavKey,
    onDestinationSelected: (NavKey) -> Unit,
    onCloseClick: () -> Unit,
    onLogoutClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showLogoutDialog by remember { mutableStateOf(false) }

    ModalDrawerSheet(modifier = modifier) {

        Image(
            painter = painterResource(R.drawable.logo_app),
            contentDescription = "Logo",
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(24.dp)
                .size(96.dp)
        )

        NavigationDrawerItem(
            selected = false,
            onClick = onCloseClick,
            icon = {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Cerrar menú"
                )
            },
            label = {
                Text(text = "Cerrar menú")
            },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )

        BOTTOM_BAR_ITEMS.forEach { (destination, item) ->
            NavigationDrawerItem(
                selected = currentDestination == destination,
                onClick = {
                    onDestinationSelected(destination)
                },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.description,
                    )
                },
                label = {
                    Text(text = item.description)
                },
                colors = NavigationDrawerItemDefaults.colors(
                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                    selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                    selectedTextColor = MaterialTheme.colorScheme.onPrimary
                ),
                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        HorizontalDivider()

        NavigationDrawerItem(
            selected = false,
            onClick = {
                showLogoutDialog = true
            },
            icon = {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Logout,
                    contentDescription = "Cerrar sesión"
                )
            },
            label = {
                Text(text = "Cerrar sesión")
            },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )
    }

    if (showLogoutDialog) {
        MessageDialog(
            title = "Cerrar sesión",
            message = "¿Desea realmente cerrar la sesión?",
            confirmButtonText = "Sí, cerrar",
            onConfirmClick = {
                showLogoutDialog = false
                onLogoutClick()
            },
            onDismiss = {
                showLogoutDialog = false
            },
            cancelButtonText = "Cancelar"
        )
    }
}
