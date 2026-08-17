package com.upsjb.movilsantarosa.core.uicomponents

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavKey
import com.upsjb.movilsantarosa.core.navigation.component.BOTTOM_BAR_ITEMS

@Composable
fun AppNavigationDrawer(
    currentDestination: NavKey,
    onDestinationSelected: (NavKey) -> Unit,
    modifier: Modifier = Modifier
) {
    ModalDrawerSheet(modifier = modifier) {
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
                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
            )
        }
    }
}
