
package com.upsjb.movilsantarosa.ui.feature.members

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.upsjb.movilsantarosa.ui.feature.members.components.MemberList
import com.upsjb.movilsantarosa.ui.feature.members.components.MemberSearchBar
import com.upsjb.movilsantarosa.ui.feature.members.components.MembersHeader

@Composable
fun MembersScreen(
    modifier: Modifier = Modifier,
    uiState: MemberUIState,
    onMemberClick: () -> Unit,
) {

    var searchText by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.onPrimary)
    ) {

        MembersHeader()

        MemberSearchBar(
            searchText = searchText,
            onSearchChange = {
                searchText = it
            }
        )

        when (val state = uiState) {

            MemberUIState.Loading -> {

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }

            }

            is MemberUIState.Error -> {

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(state.message)
                }

            }

            is MemberUIState.Success -> {

                val filteredMembers = remember(searchText, state.members) {

                    if (searchText.isBlank()) {

                        state.members

                    } else {

                        state.members.filter {

                            it.firstname.contains(searchText, true) ||
                                    it.lastname.contains(searchText, true) ||
                                    it.dniNumber.contains(searchText)

                        }

                    }

                }

                MemberList(
                    members = filteredMembers,
                    onMemberClick = {
                        // Acción al seleccionar miembro
                    }
                )

            }
        }
    }
}
