// ui/feature/announcements/components/AnnouncementsBottomNavigation.kt
package com.upsjb.movilsantarosa.ui.feature.announcements.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.upsjb.movilsantarosa.domain.models.AnnouncementTab

@Composable
fun AnnouncementsBottomNavigation(
    currentTab: AnnouncementTab = AnnouncementTab.ANNOUNCEMENTS,
    onTabSelected: (AnnouncementTab) -> Unit = {},
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        containerColor = Color(0xFF1A237E)
    ) {
        val items = listOf(
            AnnouncementTab.HOME to "Inicio",
            AnnouncementTab.MEMBERS to "Socios",
            AnnouncementTab.PAYMENTS to "Pagos",
            AnnouncementTab.FINES to "Multas",
            AnnouncementTab.ANNOUNCEMENTS to "Anuncios"
        )

        items.forEach { (tab, label) ->
            NavigationBarItem(
                selected = currentTab == tab,
                onClick = { onTabSelected(tab) },
                icon = {
                    Icon(
                        imageVector = when (tab) {
                            AnnouncementTab.HOME -> Icons.Default.Home
                            AnnouncementTab.MEMBERS -> Icons.Default.Person
                            AnnouncementTab.PAYMENTS -> Icons.Default.Payment
                            AnnouncementTab.FINES -> Icons.Default.Warning
                            AnnouncementTab.ANNOUNCEMENTS -> Icons.Default.Add
                        },
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

@Preview(showBackground = true, name = "Announcements Bottom Navigation")
@Composable
fun PreviewAnnouncementsBottomNavigation() {
    AnnouncementsBottomNavigation(currentTab = AnnouncementTab.ANNOUNCEMENTS)
}

@Preview(showBackground = true, name = "Announcements Bottom Navigation - Home Selected")
@Composable
fun PreviewAnnouncementsBottomNavigationHome() {
    AnnouncementsBottomNavigation(currentTab = AnnouncementTab.HOME)
}