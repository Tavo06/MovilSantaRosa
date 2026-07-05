package com.upsjb.movilsantarosa.feature.fine.ui.fine

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.upsjb.movilsantarosa.core.navigation.component.FinesDestination
import com.upsjb.movilsantarosa.core.navigation.component.NavigationState
import com.upsjb.movilsantarosa.core.uicomponents.AppSearchBar
import com.upsjb.movilsantarosa.core.uicomponents.ErrorSection
import com.upsjb.movilsantarosa.core.uicomponents.SkeletonSection
import com.upsjb.movilsantarosa.feature.fine.data.model.FineReason
import com.upsjb.movilsantarosa.feature.fine.domain.model.Fine
import com.upsjb.movilsantarosa.feature.fine.ui.fine.components.FineList

@Composable
fun FinesScreen(
    modifier: Modifier = Modifier,
    onFineClick: (Fine) -> Unit,
    viewModel: FineViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .padding(
                start = 16.dp,
                end = 16.dp,
                top = 16.dp
            )
            .fillMaxSize()
    ) {
        when (val state = uiState) {

            FineUiState.Loading -> {
                SkeletonSection(
                    modifier = Modifier
                        .fillMaxSize()
                )
            }

            is FineUiState.Error -> {
                ErrorSection(
                    modifier = Modifier
                        .fillMaxSize(),
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

                AppSearchBar(
                    query = state.query,
                    onQueryChange = viewModel::updateQuery,
                    placeholder = "Buscar multa"
                )

                Spacer(Modifier.height(16.dp))

                FineList(
                    fines = filteredFines,
                    onClick = onFineClick,
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
fun FinesScreenSuccessPreview() {

    val fakeFines = listOf(
        Fine(
            memberName = "Juan Perez",
            memberEmail = "juan@email.com",
            reason = FineReason.OTHER,
            amount = 50.0,
            description = "Exceso de velocidad"
        ),
        Fine(
            memberName = "Maria Lopez",
            memberEmail = "maria@email.com",
            reason = FineReason.OTHER,
            amount = 30.0,
            description = "Estacionamiento indebido"
        )
    )

    MaterialTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            AppSearchBar(
                query = "",
                onQueryChange = {},
                placeholder = "Buscar multa"
            )

            Spacer(Modifier.height(12.dp))

            FineList(
                fines = fakeFines,
                onClick = {}
            )
        }
    }
}