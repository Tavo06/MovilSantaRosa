package com.upsjb.movilsantarosa.feature.fine.ui.fine.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.upsjb.movilsantarosa.core.uicomponents.EmptySection
import com.upsjb.movilsantarosa.feature.fine.domain.model.Fine

@Composable
fun FineList(
    fines: List<Fine>,
    onClick: (Fine) -> Unit,
    modifier: Modifier = Modifier
) {
    if (fines.isEmpty()) {

        EmptySection(
            modifier = modifier.fillMaxSize(),
            title = "No hay multas registradas",
            subtitle = "Aquí aparecerán las multas cuando estén en el sistema",
        )

    } else {

        LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                bottom = 88.dp
            ),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {

            item {
                FinesDescription()
            }

            items(fines) { fine ->
                FineItem(
                    fine = fine,
                    onClick = onClick
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FineListEmptyPreview() {
    MaterialTheme {
        FineList(
            fines = emptyList(),
            onClick = {}
        )
    }
}