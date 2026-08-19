package com.upsjb.movilsantarosa.feature.post.ui.post.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.upsjb.movilsantarosa.core.uicomponents.MessageDialog
import com.upsjb.movilsantarosa.core.utils.currentTimeMillis
import com.upsjb.movilsantarosa.core.utils.toDateString
import com.upsjb.movilsantarosa.feature.post.domain.model.Post
import com.upsjb.movilsantarosa.feature.post.ui.post.PostActionState
import org.maplibre.compose.camera.CameraPosition
import org.maplibre.compose.camera.rememberCameraState
import org.maplibre.compose.map.GestureOptions
import org.maplibre.compose.map.MapOptions
import org.maplibre.compose.map.MaplibreMap
import org.maplibre.compose.map.OrnamentOptions
import org.maplibre.compose.style.BaseStyle
import org.maplibre.compose.style.rememberStyleState
import org.maplibre.spatialk.geojson.Position

@Composable
fun PostItem(
    post: Post,
    onClick: (Post) -> Unit,
    onViewMapClick: (Post) -> Unit = {},
    isAdmin: Boolean = false,
    actionState: PostActionState = PostActionState.Idle,
    onDeleteConfirmed: () -> Unit = {},
    onResetAction: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var showDeleteConfirm by remember { mutableStateOf(false) }
    var awaitingResult by remember { mutableStateOf(false) }

    LaunchedEffect(actionState) {
        if (awaitingResult && actionState is PostActionState.Success) {
            awaitingResult = false
        }
    }

    Card(
        modifier = modifier
            .fillMaxWidth(),
        onClick = { onClick(post) },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Surface(
                    modifier = Modifier.size(44.dp),
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = post.createdBy
                                .takeIf { it.isNotBlank() }
                                ?.take(2)
                                ?.uppercase()
                                ?: "NA",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = post.createdBy.ifBlank { "Sistema" },
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = post.type.displayName,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary
                        )

                        Text(
                            text = "• ${
                                post.priority.name.lowercase().replaceFirstChar { it.uppercase() }
                            }",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Column(horizontalAlignment = Alignment.End) {

                    Text(
                        text = post.createdAt.toDateString(),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    if (isAdmin) {
                        IconButton(
                            onClick = { showDeleteConfirm = true },
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Eliminar anuncio",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            }

            Text(
                text = post.title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            if (post.description.isNotBlank()) {
                Text(
                    text = post.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            val locationChip = deriveLocationChip(post.address)

            if (locationChip != null) {
                Surface(
                    shape = RoundedCornerShape(50),
                    color = MaterialTheme.colorScheme.primary
                ) {
                    Text(
                        text = locationChip,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }

            if (post.address.isNotBlank()) {
                Text(
                    text = "📍 ${post.address}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (post.latitude != null && post.longitude != null) {
                PostMapPreview(
                    latitude = post.latitude,
                    longitude = post.longitude
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onViewMapClick(post) },
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "⭐",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Ver Mapa Completo",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            if (post.expiredAt > 0L) {
                val isActive = post.expiredAt > currentTimeMillis()

                Text(
                    text = if (isActive) {
                        "Vigente hasta: ${post.expiredAt.toDateString(pattern = "dd/MM/yyyy HH:mm")}"
                    } else {
                        "Expiró: ${post.expiredAt.toDateString(pattern = "dd/MM/yyyy HH:mm")}"
                    },
                    style = MaterialTheme.typography.labelSmall,
                    color = if (isActive) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.error
                    }
                )
            }
        }
    }

    if (showDeleteConfirm) {
        MessageDialog(
            title = "¿Deseas eliminar este anuncio?",
            message = "Esta acción no se puede deshacer.",
            confirmButtonText = "Eliminar",
            cancelButtonText = "Cancelar",
            onConfirmClick = {
                showDeleteConfirm = false
                awaitingResult = true
                onDeleteConfirmed()
            },
            onDismiss = { showDeleteConfirm = false }
        )
    }

    if (awaitingResult && actionState is PostActionState.Error) {
        MessageDialog(
            title = "Aviso",
            message = actionState.message,
            confirmButtonText = "Aceptar",
            onConfirmClick = {
                awaitingResult = false
                onResetAction()
            },
            onDismiss = {
                awaitingResult = false
                onResetAction()
            }
        )
    }
}

@Composable
private fun PostMapPreview(
    latitude: Double,
    longitude: Double,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(160.dp)
            .clip(RoundedCornerShape(12.dp))
    ) {

        val cameraState = rememberCameraState(
            CameraPosition(
                target = Position(longitude = longitude, latitude = latitude),
                zoom = 15.0
            )
        )

        MaplibreMap(
            baseStyle = BaseStyle.Uri("https://tiles.openfreemap.org/styles/liberty"),
            cameraState = cameraState,
            styleState = rememberStyleState(),
            options = MapOptions(
                ornamentOptions = OrnamentOptions.AllDisabled,
                gestureOptions = GestureOptions(
                    isScrollEnabled = false,
                    isZoomEnabled = false,
                    isRotateEnabled = false,
                    isTiltEnabled = false
                )
            )
        )

        Surface(
            modifier = Modifier
                .align(Alignment.Center)
                .size(32.dp),
            shape = CircleShape,
            color = MaterialTheme.colorScheme.primary
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = "A",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}

private fun deriveLocationChip(address: String): String? {
    val segments = address.split(",")
        .map { it.trim() }
        .filter { it.isNotBlank() }

    if (segments.size < 2) return null

    val candidate = segments[segments.size - 2]
        .replace(Regex("\\d+$"), "")
        .trim()

    return candidate.ifBlank { null }
}
