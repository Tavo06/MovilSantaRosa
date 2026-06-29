// ui/feature/members/components/MembersBottomNavigation.kt
package com.upsjb.movilsantarosa.ui.feature.members.components

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.upsjb.movilsantarosa.R
import com.upsjb.movilsantarosa.domain.models.HomeTab
import com.upsjb.movilsantarosa.domain.models.MemberTab

@Composable
fun MembersBottomNavigation(
    currentTab: MemberTab = MemberTab.MEMBERS,
    onTabSelected: (MemberTab) -> Unit = {},
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        containerColor = Color(0xFF1A237E)
    ) {
        val items = listOf(
            MemberTab.HOME to "Inicio",
            MemberTab.MEMBERS to "Socios",
            MemberTab.PAYMENTS to "Pagos",
            MemberTab.FINES to "Multas",
            MemberTab.ANNOUNCEMENTS to "Anuncios"
        )


        items.forEach { (tab, label) ->
            NavigationBarItem(
                selected = currentTab == tab,
                onClick = { onTabSelected(tab) },
                icon = {
                    Icon(
                        imageVector = when (tab) {
                            MemberTab.HOME -> Icons.Default.Home
                            MemberTab.MEMBERS -> Icons.Default.Accessibility
                            MemberTab.PAYMENTS -> Icons.Default.Add
                            MemberTab.FINES -> Icons.Default.AcUnit
                            MemberTab.ANNOUNCEMENTS -> Icons.Default.Error
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

@Preview(showBackground = true, name = "Members Bottom Navigation")
@Composable
fun PreviewMembersBottomNavigation() {
    MembersBottomNavigation(currentTab = MemberTab.MEMBERS)
}

@Preview(showBackground = true, name = "Members Bottom Navigation - Payments Selected")
@Composable
fun PreviewMembersBottomNavigationPayments() {
    MembersBottomNavigation(currentTab = MemberTab.PAYMENTS)
}