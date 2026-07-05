package com.upsjb.movilsantarosa.feature.members.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.upsjb.movilsantarosa.core.uicomponents.AppSearchBar
import com.upsjb.movilsantarosa.core.uicomponents.BoxShimmer
import com.upsjb.movilsantarosa.core.uicomponents.ErrorSection
import com.upsjb.movilsantarosa.feature.members.ui.components.MemberList
import com.upsjb.movilsantarosa.feature.members.domain.model.Member
import kotlin.collections.filter

@Composable
fun MembersScreen(
    modifier: Modifier = Modifier,
    onMemberClick: (Member) -> Unit,
    viewModel: MemberViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        when (val state = uiState) {
            MemberUIState.Loading -> {
                MembersSkeleton(modifier)
            }

            is MemberUIState.Error -> {
                ErrorSection(
                    modifier = modifier,
                    title = state.message,
                    onRetry = { viewModel.getAllMembers() },
                )
            }

            is MemberUIState.Success -> {
                val filteredMembers = remember(state.query, state.members) {

                    if (state.query.isBlank()) {
                        state.members
                    } else {
                        state.members.filter {
                            it.firstname.contains(state.query, true) ||
                                    it.fullName.contains(state.query, true) ||
                                    it.dniNumber.contains(state.query)
                        }
                    }
                }
                Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    AppSearchBar(
                        query = state.query,
                        onQueryChange = viewModel::updateQuery,
                        placeholder = "Buscar socio"
                    )

                    MemberList(
                        modifier = modifier,
                        members = filteredMembers,
                        onMemberClick = onMemberClick
                    )
                }
            }
        }
    }
}

@Composable
fun MembersSkeleton(modifier: Modifier = Modifier) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(12.dp)) {
        repeat(10) {
            BoxShimmer(
                Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            )
        }
    }
}