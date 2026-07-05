package com.upsjb.movilsantarosa.core.uicomponents

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.upsjb.movilsantarosa.feature.fine.data.model.FineReason

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> FormDropdown(
    value: T,
    onValueChange: (T) -> Unit,
    label: String,
    options: List<T>,
    labelProvider: (T) -> String,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true
) {

    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded && isEnabled,
        onExpandedChange = {
            if (isEnabled) expanded = !expanded
        },
        modifier = modifier.fillMaxWidth()
    ) {

        OutlinedTextField(
            value = labelProvider(value),
            onValueChange = {},
            readOnly = true,
            enabled = isEnabled,
            label = { Text(label) },
            modifier = Modifier.menuAnchor(),
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded
                )
            }
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {

            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(labelProvider(option)) },
                    onClick = {
                        onValueChange(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "FormDropdown Preview")
@Composable
fun FormDropdownPreview() {

    var selected by remember { mutableStateOf(FineReason.LATE_PAYMENT) }

    MaterialTheme {
        Surface {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {

                Text(
                    text = "Selector de motivo",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(12.dp))

                FormDropdown(
                    value = selected,
                    onValueChange = { selected = it },
                    label = "Motivo de multa",
                    options = FineReason.entries,
                    labelProvider = { it.displayName },
                    isEnabled = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Seleccionado: ${selected.displayName}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "FormDropdown Disabled")
@Composable
fun FormDropdownDisabledPreview() {

    MaterialTheme {
        Surface {
            FormDropdown(
                value = FineReason.MISCONDUCT,
                onValueChange = {},
                label = "Motivo (solo lectura)",
                options = FineReason.entries,
                labelProvider = { it.displayName },
                isEnabled = false
            )
        }
    }
}