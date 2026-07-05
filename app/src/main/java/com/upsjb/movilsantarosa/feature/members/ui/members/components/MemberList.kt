package com.upsjb.movilsantarosa.feature.members.ui.members.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.upsjb.movilsantarosa.core.uicomponents.EmptySection
import com.upsjb.movilsantarosa.feature.members.domain.model.Member

@Composable
fun MemberList(
    members: List<Member>,
    onMemberClick: (Member) -> Unit,
    modifier: Modifier = Modifier
) {
    if (members.isEmpty()) {

        EmptySection(
            modifier = modifier.fillMaxSize(),
            title = "No se encontraron resultados",
            subtitle = "Aquí aparecerán las personas cuando estén en el sistema."
        )

    } else {

        LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                bottom = 88.dp
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            items(
                items = members,
                key = { it.email }
            ) { member ->
                MemberItem(
                    member = member,
                    onClick = { onMemberClick(member) }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MemberListEmptyPreview() {
    MaterialTheme {
        MemberList(
            members = emptyList(),
            onMemberClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MemberItemPreview() {

    val member = Member(
        firstname = "Juan",
        lastname = "Perez",
        email = "juan@email.com",
        dniNumber = "12345678"
    )

    MaterialTheme {
        MemberItem(
            member = member,
            onClick = {}
        )
    }
}