package com.upsjb.movilsantarosa.ui.common.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun FormDatePicker(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    isError: Boolean = false
) {
    Box(
        modifier = modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            readOnly = true,
            isError = isError,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun FormDatePickerPreview() {
    FormDatePicker(
        value = "28/06/2026",
        onValueChange = {},
        label = "Fecha de Nacimiento"
    )
}