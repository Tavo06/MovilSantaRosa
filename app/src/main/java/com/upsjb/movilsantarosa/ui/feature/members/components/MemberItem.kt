// ui/feature/members/components/MemberItem.kt
package com.upsjb.movilsantarosa.ui.feature.members.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.upsjb.movilsantarosa.domain.models.Member

@Composable
fun MemberItem(
    member: Member,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(
                        if (member.isActive)
                            Color(0xFF4CAF50).copy(alpha = 0.2f)
                        else
                            Color(0xFFF44336).copy(alpha = 0.2f)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = member.fullName.take(2).uppercase(),
                    fontWeight = FontWeight.Bold,
                    color = if (member.isActive) Color(0xFF4CAF50) else Color(0xFFF44336),
                    fontSize = 16.sp
                )
            }

            // Información del socio
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 12.dp)
            ) {
                Text(
                    text = member.fullName,
                    fontWeight = FontWeight.Medium,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF1A237E)
                )
                Text(
                    text = "DNI: ${member.dni}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }

            // Estado
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = if (member.isActive)
                    Color(0xFF4CAF50).copy(alpha = 0.15f)
                else
                    Color(0xFFF44336).copy(alpha = 0.15f)
            ) {
                Text(
                    text = if (member.isActive) "Activo" else "Inactivo",
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                    fontSize = 12.sp,
                    color = if (member.isActive) Color(0xFF4CAF50) else Color(0xFFF44336),
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "Member Item - Active")
@Composable
fun PreviewMemberItemActive() {
    MemberItem(
        member = Member(
            id = "1",
            fullName = "Juan Pérez Gómez",
            dni = "40150411",
            isActive = true
        ),
        onClick = {}
    )
}

@Preview(showBackground = true, name = "Member Item - Inactive")
@Composable
fun PreviewMemberItemInactive() {
    MemberItem(
        member = Member(
            id = "2",
            fullName = "Carlos Huamán Roca",
            dni = "46789673",
            isActive = false
        ),
        onClick = {}
    )
}

@Preview(showBackground = true, name = "Member Item - Long Name")
@Composable
fun PreviewMemberItemLongName() {
    MemberItem(
        member = Member(
            id = "3",
            fullName = "María del Carmen Rodríguez Fernández",
            dni = "48901234",
            isActive = true
        ),
        onClick = {}
    )
}