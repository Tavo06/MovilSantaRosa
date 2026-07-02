// ui/feature/home/components/SummaryStatsSection.kt
package com.upsjb.movilsantarosa.ui.feature.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SummaryStatsSection(
    activeMembers: Int = 20,
    debtors: Int = 5,
    paymentsOnTime: Int = 15,
    pendingFines: Int = 3,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Resumen general",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1A237E)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFE8EAF6)
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Socios activos",
                        fontSize = 19.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF1A237E)
                    )

                    Text(
                        text = activeMembers.toString(),
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF4CAF50)
                    )
                }

                HorizontalDivider()

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Deudores",
                        fontSize = 19.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF1A237E)
                    )

                    Text(
                        text = debtors.toString(),
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFF44336)
                    )
                }

                HorizontalDivider()

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Socios al dia",
                        fontSize = 19.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF1A237E)
                    )

                    Text(
                        text = paymentsOnTime.toString(),
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2196F3)
                    )
                }

                HorizontalDivider()

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Anuncios",
                        fontSize = 19.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF1A237E)
                    )

                    Text(
                        text = pendingFines.toString(),
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFF9800)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "Summary Stats Section")
@Composable
fun PreviewSummaryStatsSection() {
    SummaryStatsSection()
}

@Preview(showBackground = true, name = "Summary Stats - All Zero")
@Composable
fun PreviewSummaryStatsAllZero() {
    SummaryStatsSection(
        activeMembers = 0,
        debtors = 0,
        paymentsOnTime = 0,
        pendingFines = 0
    )
}

@Preview(showBackground = true, name = "Summary Stats - Different Values")
@Composable
fun PreviewSummaryStatsDifferent() {
    SummaryStatsSection(
        activeMembers = 45,
        debtors = 12,
        paymentsOnTime = 33,
        pendingFines = 8
    )
}