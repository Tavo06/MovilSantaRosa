package com.upsjb.movilsantarosa.core.uicomponents

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun AppPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    leadingIcon: (@Composable (() -> Unit))? = null,
    trailingIcon: (@Composable (() -> Unit))? = null
) {
    val safeOnClick = rememberClearFocusAndHideKeyboard(onClick)

    Button(
        onClick = safeOnClick,
        enabled = enabled && !isLoading,
        modifier = modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        )
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                strokeWidth = 2.dp,
                color = MaterialTheme.colorScheme.onPrimary
            )
        } else {

            leadingIcon?.invoke()

            if (leadingIcon != null) {
                Spacer(Modifier.width(8.dp))
            }

            Text(text)

            if (trailingIcon != null) {
                Spacer(Modifier.width(8.dp))
            }

            trailingIcon?.invoke()
        }
    }
}
@Composable
fun AppSecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    leadingIcon: (@Composable (() -> Unit))? = null,
    trailingIcon: (@Composable (() -> Unit))? = null
) {
    val safeOnClick = rememberClearFocusAndHideKeyboard(onClick)

    FilledTonalButton(
        onClick = safeOnClick,
        enabled = enabled && !isLoading,
        modifier = modifier.fillMaxWidth(),
        colors = ButtonDefaults.filledTonalButtonColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
        )
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                strokeWidth = 2.dp,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
        } else {

            leadingIcon?.invoke()

            if (leadingIcon != null) {
                Spacer(Modifier.width(8.dp))
            }

            Text(text)

            if (trailingIcon != null) {
                Spacer(Modifier.width(8.dp))
            }

            trailingIcon?.invoke()
        }
    }
}

@Composable
fun AppTertiaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    leadingIcon: (@Composable (() -> Unit))? = null,
    trailingIcon: (@Composable (() -> Unit))? = null
) {
    val safeOnClick = rememberClearFocusAndHideKeyboard(onClick)

    Button(
        onClick = safeOnClick,
        enabled = enabled && !isLoading,
        modifier = modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.tertiary,
            contentColor = MaterialTheme.colorScheme.onTertiary
        )
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                strokeWidth = 2.dp,
                color = MaterialTheme.colorScheme.onTertiary
            )
        } else {

            leadingIcon?.invoke()

            if (leadingIcon != null) {
                Spacer(Modifier.width(8.dp))
            }

            Text(text)

            if (trailingIcon != null) {
                Spacer(Modifier.width(8.dp))
            }

            trailingIcon?.invoke()
        }
    }
}

@Composable
fun AppOutlinedButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    leadingIcon: (@Composable (() -> Unit))? = null,
    trailingIcon: (@Composable (() -> Unit))? = null
) {
    val safeOnClick = rememberClearFocusAndHideKeyboard(onClick)

    OutlinedButton(
        onClick = safeOnClick,
        enabled = enabled && !isLoading,
        modifier = modifier.fillMaxWidth(),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = MaterialTheme.colorScheme.primary
        ),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outline
        )
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                strokeWidth = 2.dp,
                color = MaterialTheme.colorScheme.primary
            )
        } else {

            leadingIcon?.invoke()

            if (leadingIcon != null) {
                Spacer(Modifier.width(8.dp))
            }

            Text(text)

            if (trailingIcon != null) {
                Spacer(Modifier.width(8.dp))
            }

            trailingIcon?.invoke()
        }
    }
}

@Composable
private fun rememberClearFocusAndHideKeyboard(onClick: () -> Unit): () -> Unit {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    return {
        keyboardController?.hide()
        focusManager.clearFocus(force = true)
        onClick()
    }
}

@Preview(showBackground = true)
@Composable
fun AppPrimaryButtonPreview() {
    AppPrimaryButton(
        text = "Guardar",
        onClick = {}
    )
}

@Preview(showBackground = true)
@Composable
fun AppSecondaryButtonPreview() {
    AppSecondaryButton(
        text = "Cancelar",
        onClick = {}
    )
}

@Preview(showBackground = true)
@Composable
fun AppTertiaryButtonPreview() {
    AppTertiaryButton(
        text = "Continuar",
        onClick = {}
    )
}

@Preview(showBackground = true)
@Composable
fun AppOutlinedButtonPreview() {
    AppOutlinedButton(
        text = "Cancelar",
        onClick = {}
    )
}