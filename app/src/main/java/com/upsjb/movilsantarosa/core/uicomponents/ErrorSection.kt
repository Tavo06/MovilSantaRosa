package com.upsjb.movilsantarosa.core.uicomponents

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.upsjb.movilsantarosa.ui.common.components.AppPrimaryButton

enum class ErrorSectionType {
    COLUMN,
    ROW
}

private val ErrorSectionType.imageSize: Dp
    get() = when (this) {
        ErrorSectionType.COLUMN -> 160.dp
        ErrorSectionType.ROW -> 72.dp
    }

private val ErrorSectionType.titleStyle: TextStyle
    @Composable get() = when (this) {
        ErrorSectionType.COLUMN -> MaterialTheme.typography.headlineSmall
        ErrorSectionType.ROW -> MaterialTheme.typography.titleMedium
    }

private val ErrorSectionType.descriptionStyle: TextStyle
    @Composable get() = when (this) {
        ErrorSectionType.COLUMN -> MaterialTheme.typography.bodyLarge
        ErrorSectionType.ROW -> MaterialTheme.typography.bodyMedium
    }

private val ErrorSectionType.buttonHeight: Dp
    get() = when (this) {
        ErrorSectionType.COLUMN -> 48.dp
        ErrorSectionType.ROW -> 36.dp
    }

@Composable
fun ErrorSection(
    title: String,
    description: String,
    image: Painter,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
    buttonText: String = "Reintentar",
    type: ErrorSectionType = ErrorSectionType.COLUMN
) {

    val imageContent: @Composable () -> Unit = {
        Image(
            painter = image,
            contentDescription = null,
            modifier = Modifier.size(type.imageSize)
        )
    }

    val textContent: @Composable ColumnScope.() -> Unit = {

        Text(
            text = title,
            style = type.titleStyle,
            fontWeight = FontWeight.Bold,
            textAlign = if (type == ErrorSectionType.COLUMN)
                TextAlign.Center
            else
                TextAlign.Start
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = description,
            style = type.descriptionStyle,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = if (type == ErrorSectionType.COLUMN)
                TextAlign.Center
            else
                TextAlign.Start
        )

        Spacer(Modifier.height(16.dp))

        AppPrimaryButton(
            modifier = Modifier
                .height(type.buttonHeight)
                .then(
                    if (type == ErrorSectionType.ROW)
                        Modifier.wrapContentWidth()
                    else
                        Modifier.fillMaxWidth()
                ),
            text = buttonText,
            onClick = onRetry
        )
    }

    when (type) {

        ErrorSectionType.COLUMN -> {

            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                imageContent()

                Spacer(Modifier.height(24.dp))

                textContent()
            }
        }

        ErrorSectionType.ROW -> {

            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                imageContent()

                Spacer(Modifier.width(16.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    textContent()
                }
            }
        }
    }
}