package com.upsjb.movilsantarosa.feature.home.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.upsjb.movilsantarosa.core.uicomponents.BoxShimmer
import com.upsjb.movilsantarosa.core.uicomponents.ErrorSection
import com.upsjb.movilsantarosa.feature.home.ui.StatsUiState

@Composable
fun SummaryStatsSection(
    statsUiState: StatsUiState,
    onRetry: () -> Unit,
    onClick: (TypeStat) -> Unit,
    modifier: Modifier = Modifier
) {
    when (statsUiState) {
        is StatsUiState.Error -> {
            ErrorSection(
                title = statsUiState.message,
                onRetry = onRetry
            )
        }

        StatsUiState.Loading -> {
            BoxShimmer(
                modifier
                    .fillMaxWidth()
                    .height(350.dp)
            )
        }

        is StatsUiState.Success -> {
            HomeStats(
                modifier = modifier,
                stats = listOf(
                    HomeStat(
                        title = "Socios activos",
                        value = statsUiState.activeMembers.toString(),
                        valueColor = Color(0xFF4CAF50),
                        type = TypeStat.ACTIVE_MEMBERS
                    ),
                    HomeStat(
                        title = "Deudores",
                        value = statsUiState.totalFines.toString(),
                        valueColor = Color(0xFFF44336),
                        type = TypeStat.DEBTORS
                    ),
                    HomeStat(
                        title = "Socios al día",
                        value = statsUiState.totalPayments.toString(),
                        valueColor = Color(0xFF2196F3),
                        type = TypeStat.PAYMENTS_ON_TIME
                    ),
                    HomeStat(
                        title = "Anuncios y Alertas",
                        value = statsUiState.totalAnnouncements.toString(),
                        valueColor = Color(0xFFFF9800),
                        type = TypeStat.PENDING_FINES
                    )
                ),
                onClick = {}
            )
        }
    }
}

data class HomeStat(
    val title: String,
    val value: String,
    val valueColor: Color,
    val type: TypeStat,
)

enum class TypeStat {
    ACTIVE_MEMBERS,
    DEBTORS,
    PAYMENTS_ON_TIME,
    PENDING_FINES,
}

@Composable
fun HomeStatItem(
    stat: HomeStat,
    modifier: Modifier = Modifier,
    showDivider: Boolean = true,
) {
    Column(modifier = modifier) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = stat.title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                text = stat.value,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = stat.valueColor
            )
        }
    }

    if (showDivider) {
        HorizontalDivider()
    }
}


@Composable
fun HomeStats(
    stats: List<HomeStat>,
    modifier: Modifier = Modifier,
    onClick: (TypeStat) -> Unit,
) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
        ) {
            Text(
                text = "Resumen general",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.outline,
                modifier = Modifier.padding(horizontal = 20.dp)
            )

            stats.forEachIndexed { index, stat ->

                HomeStatItem(
                    stat = stat,
                    showDivider = index != stats.lastIndex,
                )
            }
        }
    }
}