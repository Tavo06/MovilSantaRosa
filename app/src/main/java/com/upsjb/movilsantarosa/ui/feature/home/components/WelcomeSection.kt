package com.upsjb.movilsantarosa.ui.feature.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.upsjb.movilsantarosa.R
import com.upsjb.movilsantarosa.core.uicomponents.BoxShimmer
import com.upsjb.movilsantarosa.core.uicomponents.ErrorSection
import com.upsjb.movilsantarosa.core.uicomponents.ErrorSectionType
import com.upsjb.movilsantarosa.domain.authentic.model.User
import com.upsjb.movilsantarosa.ui.feature.home.UserUiState

@Composable
fun WelcomeSection(
    userUiState: UserUiState,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    when (userUiState) {
        is UserUiState.Error -> {
            ErrorSection(
                modifier = modifier,
                title = userUiState.message,
                onRetry = onRetry,
                type = ErrorSectionType.ROW
            )
        }

        UserUiState.Loading -> {
            BoxShimmer(
                modifier
                    .fillMaxWidth()
                    .height(80.dp)
            )
        }

        is UserUiState.Success -> {
            UserSectionSuccess(
                modifier = modifier,
                user = userUiState.user,
            )
        }
    }
}

@Composable
fun UserSectionSuccess(user: User, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onPrimary
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Bienvenido ${user.firstname}!",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = user.rol,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.outline
                )
            }

            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = user.firstname.take(2).uppercase(),
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
        }
    }
}