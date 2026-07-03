
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.upsjb.movilsantarosa.domain.member.model.Members

@Composable
fun MemberList(
    members: List<Members>,
    onMemberClick: (Members) -> Unit,
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
        LazyColumn {

            items(
                items = members,
                key = { it.email }
            ) { member ->

                MemberItem(
                    member = member,
                    onClick = {
                        onMemberClick(member)
                    }
                )

            }
        }
    }
}
