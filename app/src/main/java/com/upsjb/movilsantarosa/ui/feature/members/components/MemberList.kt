// ui/feature/members/components/MemberList.kt
package com.upsjb.movilsantarosa.ui.feature.members.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.upsjb.movilsantarosa.domain.models.Member

@Composable
fun MemberList(
    members: List<Member>,
    onMemberClick: (Member) -> Unit,
    modifier: Modifier = Modifier
) {
    if (members.isEmpty()) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center
        ) {
            Text(
                text = "No se encontraron socios",
                color = Color.Gray,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
    } else {
        LazyColumn(
            modifier = modifier,
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(4.dp)
        ) {
            items(members) { member ->
                MemberItem(
                    member = member,
                    onClick = { onMemberClick(member) }
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "Member List - With Data")
@Composable
fun PreviewMemberListWithData() {
    val members = listOf(
        Member("1", "Juan Pérez Gómez", "40150411", true),
        Member("2", "Luis Alberto Ramos", "43271234", true),
        Member("3", "Maria Torres Silva", "48679901", true),
        Member("4", "Carlos Huamán Roca", "46789673", false),
        Member("5", "Pedro García López", "47890123", true)
    )
    MemberList(
        members = members,
        onMemberClick = {}
    )
}

@Preview(showBackground = true, name = "Member List - Empty")
@Composable
fun PreviewMemberListEmpty() {
    MemberList(
        members = emptyList(),
        onMemberClick = {}
    )
}

@Preview(showBackground = true, name = "Member List - Only Inactive")
@Composable
fun PreviewMemberListOnlyInactive() {
    val members = listOf(
        Member("1", "Carlos Huamán Roca", "46789673", false),
        Member("2", "Ana María Torres", "48901234", false)
    )
    MemberList(
        members = members,
        onMemberClick = {}
    )
}