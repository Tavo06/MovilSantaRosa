package com.upsjb.movilsantarosa.feature.members.ui.members

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.upsjb.movilsantarosa.core.uicomponents.AppSearchBar
import com.upsjb.movilsantarosa.core.uicomponents.ErrorSection
import com.upsjb.movilsantarosa.core.uicomponents.SkeletonSection
import com.upsjb.movilsantarosa.feature.members.domain.model.Member
import com.upsjb.movilsantarosa.feature.members.ui.members.components.MemberList

@Composable
fun MembersScreen(
    modifier: Modifier = Modifier,
    onMemberClick: (Member) -> Unit,
    viewModel: MemberViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp, top = 16.dp)
    ) {

        when (val state = uiState) {

            MemberUiState.Loading -> {
                SkeletonSection(modifier = Modifier.fillMaxSize())
            }

            is MemberUiState.Error -> {
                ErrorSection(
                    modifier = Modifier.fillMaxSize(),
                    title = state.message,
                    onRetry = { viewModel.getAllMembers() },
                )
            }

            is MemberUiState.Success -> {

                val filteredMembers = remember(state.query, state.members) {
                    if (state.query.isBlank()) {
                        state.members
                    } else {
                        state.members.filter {
                            it.firstname.contains(state.query, true) ||
                                    it.fullName.contains(state.query, true) ||
                                    it.dniNumber.contains(state.query, true)
                        }
                    }
                }

                AppSearchBar(
                    query = state.query,
                    onQueryChange = viewModel::updateQuery,
                    placeholder = "Buscar socio"
                )

                Spacer(Modifier.height(12.dp))

                MemberList(
                    members = filteredMembers,
                    onMemberClick = onMemberClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MembersScreenSuccessPreview() {

    val fakeMembers = listOf(
        Member(
            firstname = "Juan",
            lastname = "Perez",
            email = "juan@email.com",
            dniNumber = "12345678"
        ),
        Member(
            firstname = "Maria",
            lastname = "Lopez",
            email = "maria@email.com",
            dniNumber = "87654321"
        )
    )

    val uiState = MemberUiState.Success(
        members = fakeMembers,
        query = ""
    )

    MaterialTheme {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {

            AppSearchBar(
                query = "",
                onQueryChange = {},
                placeholder = "Buscar socio"
            )

            Spacer(Modifier.height(12.dp))

            MemberList(
                members = fakeMembers,
                onMemberClick = {}
            )
        }
    }
}