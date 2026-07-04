package com.upsjb.movilsantarosa.core.uicomponents

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Buscar",
    enabled: Boolean = true,
    onSearch: (String) -> Unit = {},
    onClear: () -> Unit = {
        onQueryChange("")
    }
) {
    SearchBarDefaults.InputField(
        modifier = modifier.fillMaxWidth(),
        query = query,
        onQueryChange = { newValue ->
            val sanitized = newValue
                .replace("\n", "")
                .take(20)
            onQueryChange(sanitized)
        },
        onSearch = onSearch,
        expanded = false,
        onExpandedChange = {},
        enabled = enabled,
        placeholder = {
            Text(placeholder)
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null
            )
        },
        trailingIcon = {
            AnimatedVisibility(query.isNotBlank()) {
                IconButton(
                    onClick = onClear
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Limpiar búsqueda"
                    )
                }
            }
        }
    )
}