package com.upsjb.movilsantarosa.feature.post.ui.post_form.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Title
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.upsjb.movilsantarosa.core.uicomponents.AppHeader
import com.upsjb.movilsantarosa.core.uicomponents.AppPrimaryButton
import com.upsjb.movilsantarosa.core.uicomponents.CardContent
import com.upsjb.movilsantarosa.core.uicomponents.FormDatePicker
import com.upsjb.movilsantarosa.core.uicomponents.FormDropdown
import com.upsjb.movilsantarosa.core.uicomponents.FormTextField
import com.upsjb.movilsantarosa.core.utils.toDateString
import com.upsjb.movilsantarosa.feature.post.data.model.PostType
import com.upsjb.movilsantarosa.feature.post.data.model.Priority
import com.upsjb.movilsantarosa.feature.post.ui.post_form.PostFormMode
import com.upsjb.movilsantarosa.feature.post.ui.post_form.PostFormState
import com.upsjb.movilsantarosa.feature.post.ui.post_form.PostFormUiState

@Composable
fun PostFormContent(
    modifier: Modifier = Modifier,
    state: PostFormUiState,
    updateForm: (PostFormState.() -> PostFormState) -> Unit,
    onSaveClick: () -> Unit,
    onEditClick: () -> Unit,
    onBackClick: () -> Unit,
    openLocationPicker: () -> Unit
) {

    val form = state.form
    val isReadOnly = state.mode == PostFormMode.READ_ONLY
    val isCreateMode = state.mode == PostFormMode.CREATE

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {

        AppHeader(
            modifier = Modifier.padding(top = 16.dp),
            title = state.mode.displayName,
            onBackClick = onBackClick,
            actions = {
                if (!isCreateMode) {
                    IconButton(
                        onClick = onEditClick,
                        modifier = Modifier.background(
                            MaterialTheme.colorScheme.primary,
                            CircleShape
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }
        )

        FormTextField(
            value = form.title,
            onValueChange = { updateForm { copy(title = it) } },
            label = "Título",
            enabled = !isReadOnly,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Title,
                    contentDescription = null
                )
            }
        )

        FormDropdown(
            label = "Tipo de publicación",
            value = form.type,
            options = PostType.entries,
            labelProvider = { it.displayName },
            onValueChange = { updateForm { copy(type = it) } },
            isEnabled = !isReadOnly
        )

        FormDropdown(
            label = "Prioridad",
            value = form.priority,
            options = Priority.entries,
            labelProvider = { it.displayName },
            onValueChange = { updateForm { copy(priority = it) } },
            isEnabled = !isReadOnly
        )

        FormTextField(
            value = form.description,
            onValueChange = { updateForm { copy(description = it) } },
            label = "Descripción",
            enabled = !isReadOnly,
            singleLine = false,
            maxLength = 300,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Description,
                    contentDescription = null
                )
            }
        )

        FormTextField(
            value = form.address,
            onValueChange = { updateForm { copy(address = it) } },
            label = "Dirección (opcional)",
            enabled = !isReadOnly,
            leadingIcon = {
                IconButton(onClick = openLocationPicker) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null
                    )
                }
            }
        )

        if (form.latitude.isNotBlank() && form.longitude.isNotBlank()) {
            CardContent(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            ) {
                Text("Lat: ${form.latitude}")
                Text("Lng: ${form.longitude}")
            }
        }

        FormDatePicker(
            value = form.expiredAt,
            enabled = !isReadOnly,
            onDateSelected = { updateForm { copy(expiredAt = it.toDateString()) } },
            label = "Fecha de expiración",
            allowFutureDates = true
        )

        Spacer(Modifier.height(12.dp))

        when (state.mode) {
            PostFormMode.CREATE,
            PostFormMode.EDIT -> {
                AppPrimaryButton(
                    text = state.mode.displayButton,
                    onClick = onSaveClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp)
                )
            }

            PostFormMode.READ_ONLY -> Unit
        }
    }
}