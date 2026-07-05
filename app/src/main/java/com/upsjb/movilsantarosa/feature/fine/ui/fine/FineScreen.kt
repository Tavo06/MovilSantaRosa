package com.upsjb.movilsantarosa.feature.fine.ui.fine

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.upsjb.movilsantarosa.core.uicomponents.AppSearchBar
import com.upsjb.movilsantarosa.core.uicomponents.ErrorSection
import com.upsjb.movilsantarosa.core.uicomponents.SkeletonSection
import com.upsjb.movilsantarosa.feature.fine.domain.model.Fine
import com.upsjb.movilsantarosa.feature.fine.ui.fine.components.FineList

@Composable
fun FinesScreen(
    modifier: Modifier = Modifier,
    onFineClick: (Fine) -> Unit,
    viewModel: FineViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        when (val state = uiState) {
            FineUiState.Loading -> {
                SkeletonSection(modifier)
            }

            is FineUiState.Error -> {
                ErrorSection(
                    modifier = modifier,
                    title = state.message,
                    onRetry = { viewModel.loadFines() },
                )
            }

            is FineUiState.Success -> {
                val filteredFines = remember(state.query, state.fines) {

                    if (state.query.isBlank()) {
                        state.fines
                    } else {
                        state.fines.filter {
                            it.description.contains(state.query, true) ||
                                    it.memberName.contains(state.query, true)
                        }
                    }
                }
                Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    AppSearchBar(
                        query = state.query,
                        onQueryChange = viewModel::updateQuery,
                        placeholder = "Buscar multa"
                    )

                    FineList(
                        modifier = modifier,
                        fines = filteredFines,
                        onClick = onFineClick
                    )
                }
            }
        }
    }
}