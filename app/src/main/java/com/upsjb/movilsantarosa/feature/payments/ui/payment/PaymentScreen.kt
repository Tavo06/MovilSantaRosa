package com.upsjb.movilsantarosa.feature.payments.ui.payment

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
import com.upsjb.movilsantarosa.feature.fine.ui.fine.FineUiState
import com.upsjb.movilsantarosa.feature.fine.ui.fine.FineViewModel
import com.upsjb.movilsantarosa.feature.fine.ui.fine.components.FineList
import com.upsjb.movilsantarosa.feature.payments.domain.model.Payment
import com.upsjb.movilsantarosa.feature.payments.ui.payment.components.PaymentList

@Composable
fun PaymentsScreen(
    modifier: Modifier = Modifier,
    onPaymentClick: (Payment) -> Unit,
    viewModel: PaymentViewModel = hiltViewModel(),
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

            PaymentUiState.Loading -> {
                SkeletonSection(
                    modifier = Modifier
                        .fillMaxSize()
                )
            }

            is PaymentUiState.Error -> {
                ErrorSection(
                    modifier = Modifier
                        .fillMaxSize(),
                    title = state.message,
                    onRetry = { viewModel.loadPayments() },
                )
            }

            is PaymentUiState.Success -> {

                val filteredPayments = remember(state.query, state.payments) {
                    if (state.query.isBlank()) {
                        state.payments
                    } else {
                        state.payments.filter {
                            it.memberDniNumber.contains(state.query, true) ||
                                    it.memberName.contains(state.query, true)
                        }
                    }
                }

                AppSearchBar(
                    query = state.query,
                    onQueryChange = viewModel::updateQuery,
                    placeholder = "Buscar pago"
                )

                Spacer(Modifier.height(16.dp))

                PaymentList(
                    payments = filteredPayments,
                    onClick = onPaymentClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
            }
        }
    }
}
