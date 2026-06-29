// ui/feature/members/MembersScreen.kt
package com.upsjb.movilsantarosa.ui.feature.members

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.upsjb.movilsantarosa.domain.models.Member
import com.upsjb.movilsantarosa.domain.models.MemberTab
import com.upsjb.movilsantarosa.ui.feature.members.components.MemberList
import com.upsjb.movilsantarosa.ui.feature.members.components.MemberSearchBar
import com.upsjb.movilsantarosa.ui.feature.members.components.MembersBottomNavigation
import com.upsjb.movilsantarosa.ui.feature.members.components.MembersHeader

@Composable
fun MembersScreen(
    modifier: Modifier = Modifier
) {
    var searchText by remember { mutableStateOf("") }
    var selectedTab by remember { mutableStateOf(MemberTab.MEMBERS) }

    // Datos de ejemplo
    val allMembers = listOf(
        Member("1", "Juan Pérez Gómez", "40150411", true),
        Member("2", "Luis Alberto Ramos", "43271234", true),
        Member("3", "Maria Torres Silva", "48679901", true),
        Member("4", "Carlos Huamán Roca", "46789673", false),
        Member("5", "Pedro García López", "47890123", true)
    )

    // Filtrar socios según búsqueda
    val filteredMembers = if (searchText.isEmpty()) {
        allMembers
    } else {
        allMembers.filter {
            it.fullName.contains(searchText, ignoreCase = true) ||
                    it.dni.contains(searchText)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        // Header fijo
        MembersHeader()

        // Barra de búsqueda
        MemberSearchBar(
            searchText = searchText,
            onSearchChange = { searchText = it },
            onAddMember = { /* Acción para agregar socio */ }
        )

        // Lista de socios con scroll
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            MemberList(
                members = filteredMembers,
                onMemberClick = { /* Acción al seleccionar un socio */ }
            )

            // Espacio adicional
            androidx.compose.foundation.layout.Spacer(
                modifier = Modifier.fillMaxWidth().height(16.dp)
            )
        }

        // Bottom Navigation
        MembersBottomNavigation(
            currentTab = selectedTab,
            onTabSelected = { selectedTab = it }
        )
    }
}

@Preview(showBackground = true, name = "Members Screen - Full")
@Composable
fun PreviewMembersScreen() {
    MembersScreen()
}

@Preview(showBackground = true, name = "Members Screen - With Search")
@Composable
fun PreviewMembersScreenWithSearch() {
    MembersScreen()
    // En la preview no podemos modificar el estado fácilmente,
    // pero en la implementación real funcionaría correctamente
}

@Preview(showBackground = true, name = "Members Screen - Empty Results")
@Composable
fun PreviewMembersScreenEmptyResults() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        MembersHeader()

        MemberSearchBar(
            searchText = "XYZ",
            onSearchChange = {},
            onAddMember = {}
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            MemberList(
                members = emptyList(),
                onMemberClick = {}
            )
        }

        MembersBottomNavigation(currentTab = MemberTab.MEMBERS)
    }
}