package com.upsjb.movilsantarosa.feature.fine.ui.fine.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
            title = "No hay multas registradas",
            subtitle = "Aquí aparecerán las multas cuando estén en el sistema",
            modifier = modifier
        )
    } else {
        LazyColumn(
            modifier = modifier,
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


@Preview(showBackground = true, name = "Fine List - Empty")
@Composable
fun PreviewFineListEmpty() {
    FineList(
        fines = emptyList(),
        onClick = {})
}