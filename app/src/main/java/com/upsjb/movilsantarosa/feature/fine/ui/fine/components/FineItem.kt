package com.upsjb.movilsantarosa.feature.fine.ui.fine.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.upsjb.movilsantarosa.core.utils.toCurrencyString
import com.upsjb.movilsantarosa.feature.fine.data.model.FineReason
import com.upsjb.movilsantarosa.feature.fine.data.model.FineStatus
import com.upsjb.movilsantarosa.feature.fine.domain.model.Fine

@Composable
fun FineItem(
    fine: Fine,
    onClick: (Fine) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        onClick = { onClick(fine) },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Surface(
                modifier = Modifier.size(56.dp),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.errorContainer
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = fine.memberName.take(2).uppercase(),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = fine.memberName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "DNI: ${fine.memberDniNumber}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = fine.reason.displayName +
                            if (fine.customReason.isNotBlank())
                                " - ${fine.customReason}"
                            else "",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                if (fine.dueDate.isNotEmpty()) {
                    Text(
                        text = "Vence: ${fine.dueDate}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }

            Column(
                horizontalAlignment = Alignment.End
            ) {

                Text(
                    text = fine.amount.toCurrencyString(),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.error
                )

                Spacer(modifier = Modifier.height(6.dp))

                FineStatusChip(status = fine.status)
            }
        }
    }
}

@Composable
fun FineStatus.containerColor(): Color = when (this) {
    FineStatus.PENDING -> MaterialTheme.colorScheme.tertiary
    FineStatus.PAID -> MaterialTheme.colorScheme.primary
    FineStatus.CANCELLED -> MaterialTheme.colorScheme.outline
}

@Composable
fun FineStatusChip(
    status: FineStatus
) {
    val color = status.containerColor()

    Surface(
        shape = RoundedCornerShape(12.dp),
        color = color.copy(alpha = 0.15f)
    ) {
        Text(
            text = status.displayName,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall,
            color = color,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Preview(showBackground = true)
@Composable
fun FineItemPreview() {

    val fine = Fine(
        memberName = "Juan Perez",
        memberEmail = "juan@email.com",
        reason = FineReason.OTHER,
        amount = 100.0,
        description = "Incumplimiento de normas"
    )

    MaterialTheme {
        FineItem(
            fine = fine,
            onClick = {}
        )
    }
}