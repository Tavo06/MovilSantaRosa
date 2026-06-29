// ui/feature/home/components/HomeBottomNavigation.kt
package com.upsjb.movilsantarosa.ui.feature.home.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.Accessibility
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.upsjb.movilsantarosa.R
import com.upsjb.movilsantarosa.domain.models.HomeTab

@Composable
fun HomeBottomNavigation(
    currentTab: HomeTab = HomeTab.HOME,
    onTabSelected: (HomeTab) -> Unit = {},
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        containerColor = Color(0xFF1A237E)
    ) {
        val items = listOf(
            HomeTab.HOME to "Inicio",
            HomeTab.MEMBERS to "Socios",
            HomeTab.PAYMENTS to "Pagos",
            HomeTab.FINES to "Multas",
            HomeTab.ANNOUNCEMENTS to "Anuncios"
        )

        items.forEach { (tab, label) ->
            NavigationBarItem(
                selected = currentTab == tab,
                onClick = { onTabSelected(tab) },
                icon = {
                    Icon(
                        imageVector = when (tab) {
                                HomeTab.HOME -> Icons.Default.Home
                                HomeTab.MEMBERS -> Icons.Default.Accessibility
                                HomeTab.PAYMENTS -> Icons.Default.Add
                                HomeTab.FINES -> Icons.Default.AcUnit
                                HomeTab.ANNOUNCEMENTS -> Icons.Default.Error
                            }
                        ,
                        contentDescription = label,
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = {
                    Text(
                        text = label,
                        fontSize = 10.sp
                    )
                },
                colors = androidx.compose.material3.NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.White,
                    unselectedIconColor = Color.White.copy(alpha = 0.6f),
                    selectedTextColor = Color.White,
                    unselectedTextColor = Color.White.copy(alpha = 0.6f)
                )
            )
        }
    }
}

@Preview(showBackground = true, name = "Bottom Navigation - Home Selected")
@Composable
fun PreviewBottomNavigationHome() {
    HomeBottomNavigation(currentTab = HomeTab.HOME)
}

@Preview(showBackground = true, name = "Bottom Navigation - Members Selected")
@Composable
fun PreviewBottomNavigationMembers() {
    HomeBottomNavigation(currentTab = HomeTab.MEMBERS)
}

@Preview(showBackground = true, name = "Bottom Navigation - Payments Selected")
@Composable
fun PreviewBottomNavigationPayments() {
    HomeBottomNavigation(currentTab = HomeTab.PAYMENTS)
}

@Preview(showBackground = true, name = "Bottom Navigation - Fines Selected")
@Composable
fun PreviewBottomNavigationFines() {
    HomeBottomNavigation(currentTab = HomeTab.FINES)
}

@Preview(showBackground = true, name = "Bottom Navigation - Announcements Selected")
@Composable
fun PreviewBottomNavigationAnnouncements() {
    HomeBottomNavigation(currentTab = HomeTab.ANNOUNCEMENTS)
}